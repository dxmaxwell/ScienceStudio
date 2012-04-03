/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersToCDFAdpater class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.adapter;

import gsfc.nssdc.cdf.Attribute;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Entry;
import gsfc.nssdc.cdf.Variable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.cdf.CDFEvent;
import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.CDFTypesUtility;
import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.standard.StdUnits;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 * 
 *
 */
public class MapXYVespersToCDFAdapter implements MapXYVespersAdapter, StdCategories {

	public static final long DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL = 6;
	
	private static final long[] EMPTY_LONG_ARRAY = new long[0];
	
	private static final long CDF_MAX_COMPRESSION_LEVEL = 9;
	private static final long CDF_MIN_COMPRESSION_LEVEL = 0;
	
	/// Parameters to be configured by a ConverterFactory ///
	private File dataFile = null;
	private File spectraFile = null;
	private File cdfDataFile = null;
	private boolean forceUpdate = false;
	private long[] cdfSpectrumCompressionLevel = new long[] { DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL };
	//////////////////////////////////////////////////////////
	
	private CDF cdf;
	
	private int catRegEntryId = 0;
	private Attribute catRegAttribute = null;
	
	private Entry scanCatEntry = null;
	private Entry ssModelCatEntry = null;
	private Entry mapXYCatEntry = null;
	private Entry beamICatEntry = null;
	private Entry mcaCatEntry = null;
	
	private Attribute ssCreated = null;
	private Attribute ssCreatedBy = null;
	private Attribute ssSource = null;
	
	private Attribute scanStartTime = null;
	private Attribute scanEndTime = null;
	
	private Attribute ssModelProjectName = null;
	private Attribute ssModelSessionName = null;
	private Attribute ssModelFacilityName = null;
	private Attribute ssModelLaboratoryName = null;
	private Attribute ssModelExperimentName = null;
	private Attribute ssModelInstrumentName = null;
	private Attribute ssModelTechniqueName = null;
	private Attribute ssModelScanName = null;
	private Attribute ssModelSampleName = null;
	
	private Attribute mapXYName = null;
	private Attribute mapXYDesc = null;
	private Attribute mapXYUnit = null;
	
	private Attribute mapXYSixeX = null;
	private Attribute mapXYStartX = null;
	private Attribute mapXYEndX = null;
	private Attribute mapXYStepX = null;
	private Variable  mapXYPointX = null;
	private Variable  mapXYPositionX = null;
	
	private Attribute mapXYSixeY = null;
	private Attribute mapXYStartY = null;
	private Attribute mapXYEndY = null;
	private Attribute mapXYStepY = null;
	private Variable  mapXYPointY = null;
	private Variable  mapXYPositionY = null;
	
	private Attribute beamIName = null;
	private Attribute beamIDesc = null;
	private Attribute beamIUnit = null;
	
	private Variable beamISrCurrent = null;
	private Variable[] beamIMcsCurrent = new Variable[10];
	
	private Attribute mcaName = null;
	private Attribute mcaDesc = null;
	private Attribute mcaUnit = null;
	private Attribute mcaCrts = null;
	
	private Attribute mcaNElements = null;
	private Attribute mcaMaxEnergy = null;
	private Attribute mcaEnergyGapTime = null;
	private Attribute mcaEnergyPeakingTime = null;
	private Attribute mcaPresetRealTime = null;
	
	private Variable[] mcaFastCount = new Variable[4];
	private Variable[] mcaSlowCount = new Variable[4];
	private Variable[] mcaDeadTimePct = new Variable[4];
	private Variable[] mcaElapsedRealTime = new Variable[4];
	private Variable[] mcaElapsedLiveTime = new Variable[4];
	private Variable[] mcaSpectrum = new Variable[4];
	private Variable mcaSumSpectrum = null;
	
	//private CDFEvent cdfEvent = null;
	private CDFRecord cdfRecord = null;
	
	protected Log log = LogFactory.getLog(getClass());
	
	public MapXYVespersToCDFAdapter() {
		super();
	}
	
	@Override
	public boolean openDestination() throws ConverterException {
		
		if(cdfDataFile == null) {
			throw new ConverterException("Destination CDF file not specified.");
		}
		
		if(cdfDataFile.exists() && isForceUpdate()) {
			log.debug("CDF file already exists in data directory.");
			boolean cdfDelete = false;
			
			if(isForceUpdate() || (cdfDataFile.length() == 0L)) {
				log.debug("Force update or existing file length is zero.");
				cdfDelete = true;
			}
			else if(cdfDataFile.lastModified() < getDataFile().lastModified()) {
				log.debug("Data file is newer than CDF data file.");
				cdfDelete = true;
			}
			else if(cdfDataFile.lastModified() < getSpectraFile().lastModified()) {
				log.debug("Spectra file is newer than CDF data file.");
				cdfDelete = true;
			}
			
			if(cdfDelete) {
				log.debug("Attempt to delete existing CDF data file.");
				if (!cdfDataFile.delete()) {
					throw new ConverterException("Unable to remove existing CDF file.");
				}
			}
			else {
				return true;
			}
		}
		
		try {
			cdf = CDF.create(cdfDataFile.getAbsolutePath());
		}
		catch(CDFException e) {
			throw new ConverterException("Exception while creating CDF file: " + cdfDataFile, e);
		}
		
		// These attributes must be initialized after the CDF data file. // 
		
		catRegAttribute = createCdfAttribute(CATEGORY_REGISTRY, CDF.GLOBAL_SCOPE);
		
		try {
			double now = CDFTypesUtility.getCDFEpoch(); // throws CDFException //
			
			createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, SS10.getIdentity());
			
			ssCreated = createCdfAttribute(SS10.Created(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssCreated, 0, CDF.CDF_EPOCH, now);
			
			ssCreatedBy = createCdfAttribute(SS10.CreatedBy(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssCreatedBy, 0, CDF.CDF_CHAR, getClass().getName());
				
			ssSource = createCdfAttribute(SS10.Source(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssSource, 0, CDF.CDF_CHAR, getDataFile().toString());
			createCdfEntry(ssSource, 1, CDF.CDF_CHAR, getSpectraFile().toString());
		}
		catch(CDFException e) {
			// nothing to do //
		}
		
		return false;
	}
	
	@Override
	public void closeDestination() throws ConverterException {
		if(cdf != null) {
			try {
				cdf.close();
				cdf = null;
			}
			catch(CDFException e) {
				log.warn("Error while closing CDF file.", e);
			}
		}
	}

	@Override
	public void deleteDestination() throws ConverterException {
		try {
			closeDestination();
		}
		finally {
			if(cdfDataFile.exists() && !cdfDataFile.delete()) {
				log.warn("Error while deleting CDF file.");
			}
		}
	}	
	
	@Override
	public void recScanStartTime(Date startTime) throws ConverterException {
		try {
			if(scanStartTime == null) {
				scanStartTime = createCdfAttribute(Scan10.StartTime(), CDF.GLOBAL_SCOPE);
				createCdfEntry(scanStartTime, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(startTime));
				createScanCatEntry();
			}
			else {
				updateCdfEntry(scanStartTime, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(startTime));
			}
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	public void recScanEndTime(Date endTime) throws ConverterException {
		try {
			if(scanEndTime == null) {
				scanEndTime = createCdfAttribute(Scan10.EndTime(), CDF.GLOBAL_SCOPE);
				createCdfEntry(scanEndTime, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(endTime));
				createScanCatEntry();
			}
			else {
				updateCdfEntry(scanEndTime, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(endTime));
			}
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	protected void createScanCatEntry() throws ConverterException {
		if((scanCatEntry == null) && (scanStartTime != null) && scanEndTime != null) {
			scanCatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, Scan10.getIdentity());
		}
	}
	
	@Override
	public void recProjectName(String projectName) throws ConverterException {
		if(ssModelProjectName == null) {
			ssModelProjectName = createCdfAttribute(SSModel10.ProjectName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelProjectName, 0, CDF.CDF_CHAR, projectName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelProjectName, 0, CDF.CDF_CHAR, projectName);
		}
	}

	@Override
	public void recSessionName(String sessionName) throws ConverterException {
		if(ssModelSessionName == null) {
			ssModelSessionName = createCdfAttribute(SSModel10.SessionName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelSessionName, 0, CDF.CDF_CHAR, sessionName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelSessionName, 0, CDF.CDF_CHAR, sessionName);
		}
	}

	@Override
	public void recFacilityName(String facilityName) throws ConverterException {
		if(ssModelFacilityName == null) {
			ssModelFacilityName = createCdfAttribute(SSModel10.Facility(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelFacilityName, 0, CDF.CDF_CHAR, facilityName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelFacilityName, 0, CDF.CDF_CHAR, facilityName);
		}
	}

	@Override
	public void recLaboratoryName(String laboratoryName) throws ConverterException {
		if(ssModelLaboratoryName == null) {
			ssModelLaboratoryName = createCdfAttribute(SSModel10.Laboratory(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelLaboratoryName, 0, CDF.CDF_CHAR, laboratoryName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelLaboratoryName, 0, CDF.CDF_CHAR, laboratoryName);
		}
	}

	@Override
	public void recExperimentName(String experimentName) throws ConverterException {
		if(ssModelExperimentName == null) {
			ssModelExperimentName = createCdfAttribute(SSModel10.ExperimentName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelExperimentName, 0, CDF.CDF_CHAR, experimentName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelExperimentName, 0, CDF.CDF_CHAR, experimentName);
		}
	}

	@Override
	public void recInstrumentName(String instrumentName) throws ConverterException {
		if(ssModelInstrumentName == null) {
			ssModelInstrumentName = createCdfAttribute(SSModel10.Instrument(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelInstrumentName, 0, CDF.CDF_CHAR, instrumentName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelInstrumentName, 0, CDF.CDF_CHAR, instrumentName);
		}
	}

	@Override
	public void recTechniqueName(String techniqueName) throws ConverterException {
		if(ssModelTechniqueName == null) {
			ssModelTechniqueName = createCdfAttribute(SSModel10.Technique(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelTechniqueName, 0, CDF.CDF_CHAR, techniqueName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelTechniqueName, 0, CDF.CDF_CHAR, techniqueName);
		}
	}

	@Override
	public void recScanName(String scanName) throws ConverterException {
		if(ssModelScanName == null) {
			ssModelScanName = createCdfAttribute(SSModel10.ScanName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelScanName, 0, CDF.CDF_CHAR, scanName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelScanName, 0, CDF.CDF_CHAR, scanName);
		}
	}

	@Override
	public void recSampleName(String sampleName) throws ConverterException {
		if(ssModelSampleName == null) {
			ssModelSampleName = createCdfAttribute(SSModel10.SampleName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(ssModelSampleName, 0, CDF.CDF_CHAR, sampleName);
			createSSModelCatEntry();
		}
		else {
			updateCdfEntry(ssModelSampleName, 0, CDF.CDF_CHAR, sampleName);
		}
	}

	protected void createSSModelCatEntry() throws ConverterException {
		if(ssModelCatEntry == null && 
				(ssModelProjectName != null) && (ssModelSessionName != null) && (ssModelFacilityName != null) &&
				(ssModelLaboratoryName != null) && (ssModelExperimentName != null) && (ssModelInstrumentName != null) &&
				(ssModelTechniqueName != null) && (ssModelScanName != null) && (ssModelSampleName != null)) {
			ssModelCatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, SSModel10.getIdentity());
		}
	}
	
	@Override
	public void openDataRecord(int dataRecordIndex) throws ConverterException {
		
		if(cdfRecord == null) {
			Collection<Variable> cdfVariables = new ArrayList<Variable>();
				
			if(mapXYPointX != null) {
				cdfVariables.add(mapXYPointX);
			}
		
			if(mapXYPositionX != null) {
				cdfVariables.add(mapXYPositionX);
			}
			
			if(mapXYPointY != null) {
				cdfVariables.add(mapXYPointY);
			}
		
			if(mapXYPositionY != null) {
				cdfVariables.add(mapXYPositionY);
			}
			
			if(beamISrCurrent != null) {
				cdfVariables.add(beamISrCurrent);
			}

			for(Variable v : beamIMcsCurrent) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}

			for(Variable v : mcaFastCount) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}
			
			for(Variable v : mcaSlowCount) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}
			
			for(Variable v : mcaDeadTimePct) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}
			
			for(Variable v : mcaElapsedRealTime) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}
			
			for(Variable v : mcaElapsedLiveTime) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}
			
			for(Variable v : mcaSpectrum) {
				if(v != null) {
					cdfVariables.add(v);
				}
			}
			
			if(mcaSumSpectrum != null) {
				cdfVariables.add(mcaSumSpectrum);
			}
		
			cdfRecord = new CDFRecord(new CDFEvent(cdfVariables));
		}
	}
		
	@Override
	public void closeDataRecord(int dataRecordIndex) throws ConverterException {
		try {
			cdf.putRecord(dataRecordIndex, cdfRecord.getEvent().getVariableIDs(), cdfRecord.getCDFValues());
		}
		catch(CDFException e) {
			throw new ConverterException("Error while adding data record to CDF file.", e);
		}
		catch(RecordFormatException e) {
			throw new ConverterException("Error while getting data value from DAF file.", e);
		}
	}
	
	@Override
	public void initSizeX(String name, String description) throws ConverterException {
		if(mapXYSixeX == null) {
			mapXYSixeX = createCdfAttribute(MapXY11.SizeX(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYSixeX, 0, CDF.CDF_INT2, 0 /*sizeX*/);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initStepX(String name, String description) throws ConverterException {
		if(mapXYStepX == null) {
			mapXYStepX = createCdfAttribute(MapXY11.StepX(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYStepX, 0, CDF.CDF_DOUBLE, 0.0 /*stepX*/);
			createCdfEntry(mapXYStepX, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}
	
	@Override
	public void initStartX(String name, String description) throws ConverterException {
		if(mapXYStartX == null) {
			mapXYStartX = createCdfAttribute(MapXY11.StartX(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYStartX, 0, CDF.CDF_DOUBLE, 0.0 /*startX*/);
			createCdfEntry(mapXYStartX, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initEndX(String name, String description) throws ConverterException {
		if(mapXYEndX == null) {
			mapXYEndX = createCdfAttribute(MapXY11.EndX(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYEndX, 0, CDF.CDF_DOUBLE, 0.0 /*endX*/);
			createCdfEntry(mapXYEndX, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initPointX(String name, String description) throws ConverterException {
		if(mapXYPointX == null) {
			mapXYPointX = createCdfVariable(MapXY11.I(), CDF.CDF_INT2, 1, 0, EMPTY_LONG_ARRAY);
			createMapXYVarAttributes();
			createCdfEntry(mapXYDesc, mapXYPointX, CDF.CDF_CHAR, "Computed point index in X-direction.");
			createMapXYCatEntry();
		}
	}

	@Override
	public void initPositionX(String name, String description) throws ConverterException {
		if(mapXYPositionX == null) {
			mapXYPositionX = createCdfVariable(MapXY11.X(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMapXYVarAttributes();
			if(name != null) {
				createCdfEntry(mapXYName, mapXYPositionX, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mapXYDesc, mapXYPositionX, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mapXYUnit, mapXYPositionX, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void recSizeX(int sizeX) throws ConverterException {
		if(mapXYSixeX != null) {
			updateCdfEntry(mapXYSixeX, 0, CDF.CDF_UINT2, sizeX);
		}
	}
	
	@Override
	public void recStepX(double stepX) throws ConverterException {
		if(mapXYStepX != null) {
			updateCdfEntry(mapXYStepX, 0, CDF.CDF_DOUBLE, stepX);
		}
	}
	
	@Override
	public void recStartX(double startX) throws ConverterException {
		if(mapXYStartX != null) {
			updateCdfEntry(mapXYStartX, 0, CDF.CDF_DOUBLE, startX);
		}
	}
	
	@Override
	public void recEndX(double endX) throws ConverterException {
		if(mapXYEndX != null) {
			updateCdfEntry(mapXYEndX, 0, CDF.CDF_DOUBLE, endX);
		}
	}
	
	@Override
	public void recPointX(int pointX) throws ConverterException {
		if(mapXYPointX != null) {
			try {
				cdfRecord.setValueByName(MapXY11.I(), pointX);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recPositionX(double positionX) throws ConverterException {
		if(mapXYPositionX != null) {
			try {
				cdfRecord.setValueByName(MapXY11.X(), positionX);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	@Override
	public void initSizeY(String name, String description) throws ConverterException {
		if(mapXYSixeY == null) {
			mapXYSixeY = createCdfAttribute(MapXY11.SizeY(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYSixeY, 0, CDF.CDF_INT2, 0);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initStepY(String name, String description) throws ConverterException {
		if(mapXYStepY == null) {
			mapXYStepY = createCdfAttribute(MapXY11.StepY(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYStepY, 0, CDF.CDF_DOUBLE, 0.0 /*stepY*/);
			createCdfEntry(mapXYStepY, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initStartY(String name, String description) throws ConverterException {
		if(mapXYStartY == null) {
			mapXYStartY = createCdfAttribute(MapXY11.StartY(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYStartY, 0, CDF.CDF_DOUBLE, 0.0 /*startY*/);
			createCdfEntry(mapXYStartY, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initEndY(String name, String description) throws ConverterException {
		if(mapXYEndY == null) {
			mapXYEndY = createCdfAttribute(MapXY11.EndY(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mapXYEndY, 0, CDF.CDF_DOUBLE, 0.0 /*endY*/);
			createCdfEntry(mapXYEndY, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void initPointY(String name, String description) throws ConverterException {
		if(mapXYPointY == null) {
			mapXYPointY = createCdfVariable(MapXY11.J(), CDF.CDF_INT2, 1, 0, EMPTY_LONG_ARRAY);
			createMapXYVarAttributes();
			createCdfEntry(mapXYDesc, mapXYPointY, CDF.CDF_CHAR, "Computed point index in Y-direction.");
			createMapXYCatEntry();
		}
	}

	@Override
	public void initPositionY(String name, String description) throws ConverterException {
		if(mapXYPositionY == null) {
			mapXYPositionY = createCdfVariable(MapXY11.Y(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMapXYVarAttributes();
			if(name != null) {
				createCdfEntry(mapXYName, mapXYPositionY, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mapXYDesc, mapXYPositionY, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mapXYUnit, mapXYPositionY, CDF.CDF_CHAR, StdUnits.MILLI_METER);
			createMapXYCatEntry();
		}
	}

	@Override
	public void recSizeY(int sizeY) throws ConverterException {
		if(mapXYSixeY != null) {
			updateCdfEntry(mapXYSixeY, 0, CDF.CDF_UINT2, sizeY);
		}
	}
	
	@Override
	public void recStepY(double stepY) throws ConverterException {
		if(mapXYStepY != null) {
			updateCdfEntry(mapXYStepY, 0, CDF.CDF_DOUBLE, stepY);
		}
	}
	
	@Override
	public void recStartY(double startY) throws ConverterException {
		if(mapXYStartY != null) {
			updateCdfEntry(mapXYStartY, 0, CDF.CDF_DOUBLE, startY);
		}
	}

	@Override
	public void recEndY(double endY) throws ConverterException {
		if(mapXYEndY != null) {
			updateCdfEntry(mapXYEndY, 0, CDF.CDF_DOUBLE, endY);
		}
	}

	@Override
	public void recPointY(int pointY) throws ConverterException {
		if(mapXYPointY != null) {
			try {
				cdfRecord.setValueByName(MapXY11.J(), pointY);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recPositionY(double positionY) throws ConverterException {
		if(mapXYPositionY != null) {
			try {
				cdfRecord.setValueByName(MapXY11.Y(), positionY);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	protected void createMapXYVarAttributes() throws ConverterException {
		if(mapXYName == null) {
			mapXYName = createCdfAttribute(MapXY11.Name(), CDF.VARIABLE_SCOPE);
		}
		
		if(mapXYDesc == null) {
			mapXYDesc = createCdfAttribute(MapXY11.Desc(), CDF.VARIABLE_SCOPE);
		}
		
		if(mapXYUnit == null) {
			mapXYUnit = createCdfAttribute(MapXY11.Unit(), CDF.VARIABLE_SCOPE);
		}
	}
	
	protected void createMapXYCatEntry() throws ConverterException {
		if((mapXYCatEntry == null) &&
				(mapXYSixeX != null) && (mapXYStartX != null) && (mapXYEndX != null) && (mapXYStepX != null) && (mapXYPointX != null) && (mapXYPositionX != null) &&
				(mapXYSixeY != null) && (mapXYStartY != null) && (mapXYEndY != null) && (mapXYStepY != null) && (mapXYPointY != null) && (mapXYPositionY != null)) {
			mapXYCatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, MapXY11.getIdentity());
		}
	}
	
	@Override
	public void initSrCurrent(String name, String description) throws ConverterException {
		if(beamISrCurrent == null) {
			beamISrCurrent = createCdfVariable(BeamI11.SR(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createBeamIVarAttributes();
			if(name != null) {
				createCdfEntry(beamIName, beamISrCurrent, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(beamIDesc, beamISrCurrent, CDF.CDF_CHAR, description);
			}
			createCdfEntry(beamIUnit, beamISrCurrent, CDF.CDF_CHAR, StdUnits.MILLI_AMPERE);
			createBeamICatEntry();
		}
	}

	@Override
	public void initMcsCurrent(int index, String name, String description) throws ConverterException {
		if(beamIMcsCurrent[index] == null) {
			Variable v = createCdfVariable(BeamI11.I(index), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createBeamIVarAttributes();
			if(name != null) {
				createCdfEntry(beamIName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(beamIDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(beamIUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			beamIMcsCurrent[index] = v;
			createBeamICatEntry();
		}
	}

	@Override
	public void recSrCurrent(double srCurrent) throws ConverterException {
		if(beamISrCurrent != null) {
			try {
				cdfRecord.setValueByName(BeamI11.SR(), srCurrent);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recMcsCurrent(int index, double mcsCurrent) throws ConverterException {
		if(beamIMcsCurrent[index] != null) {
			try {
				cdfRecord.setValueByName(BeamI11.I(index), mcsCurrent);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	protected void createBeamIVarAttributes() throws ConverterException {
		if(beamIName == null) {
			beamIName = createCdfAttribute(BeamI11.Name(), CDF.VARIABLE_SCOPE);
		}
		
		if(beamIDesc == null) {
			beamIDesc = createCdfAttribute(BeamI11.Desc(), CDF.VARIABLE_SCOPE);
		}
		
		if(beamIUnit == null) {
			beamIUnit = createCdfAttribute(BeamI11.Unit(), CDF.VARIABLE_SCOPE);
		}
	}
	
	protected void createBeamICatEntry() throws ConverterException {
		if(beamICatEntry == null) {	
			if(beamISrCurrent != null) {
				beamICatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, BeamI11.getIdentity());
			}
			else {
				for(Variable v : beamIMcsCurrent) {
					if(v != null) {
						beamICatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, BeamI11.getIdentity());
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void initSedFastCount(String name, String description) throws ConverterException {
		if(mcaFastCount[0] == null) {
			Variable v = createCdfVariable(MCA10.FastCount(0), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaFastCount[0] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initSedSlowCount(String name, String description) throws ConverterException {
		if(mcaSlowCount[0] == null) {
			Variable v = createCdfVariable(MCA10.SlowCount(0), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaSlowCount[0] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initSedDeadTimePct(String name, String description) throws ConverterException {
		if(mcaDeadTimePct[0] == null) {
			Variable v = createCdfVariable(MCA10.DeadTimePct(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.PERCENT);
			mcaDeadTimePct[0] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initSedElapsedRealTime(String name, String description) throws ConverterException {
		if(mcaElapsedRealTime[0] == null) {
			Variable v = createCdfVariable(MCA10.ElapsedRealTime(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.SECOND);
			mcaElapsedRealTime[0] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initSedElapsedLiveTime(String name, String description) throws ConverterException {
		if(mcaElapsedLiveTime[0] == null) {
			Variable v = createCdfVariable(MCA10.ElapsedLiveTime(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.SECOND);
			mcaElapsedLiveTime[0] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initSedSpectrum(int nChannels, String name, String description) throws ConverterException {
		if(mcaNElements == null) {
			mcaNElements = createCdfAttribute(MCA10.NElements(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mcaNElements, 0, CDF.CDF_INT2, 1);
			createMCACatEntry();
		}
		
		if(mcaSpectrum[0] == null) {
			Variable v = createCdfVariable(MCA10.Spectrum(0), CDF.CDF_UINT4, 1, 1, new long[] { nChannels });
			createMCAVarAttributes();
			if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
				try {
					v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
				}
				catch(CDFException e) {
					throw new ConverterException("CDF Set Variable Compression: Level: " + cdfSpectrumCompressionLevel, e);
				}
			}
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaSpectrum[0] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void recSedFastCount(long sedFastCount) throws ConverterException {
		if(mcaFastCount[0] != null) {
			try {
				cdfRecord.setValueByName(MCA10.FastCount(0), sedFastCount);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	@Override
	public void recSedSlowCount(long sedSlowCount) throws ConverterException {
		if(mcaSlowCount[0] != null) {
			try {
				cdfRecord.setValueByName(MCA10.SlowCount(0), sedSlowCount);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	@Override
	public void recSedDeadTimePct(double sedDeadTimePct) throws ConverterException {
		if(mcaDeadTimePct[0] != null) {
			try {
				cdfRecord.setValueByName(MCA10.DeadTimePct(0), sedDeadTimePct);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	@Override
	public void recSedElapsedRealTime(double sedElapsedRealTime) throws ConverterException {
		if(mcaElapsedRealTime[0] != null) {
			try {
				cdfRecord.setValueByName(MCA10.ElapsedRealTime(0), sedElapsedRealTime);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recSedElapsedLiveTime(double sedElapsedLiveTime) throws ConverterException {
		if(mcaElapsedLiveTime[0] != null) {
			try {
				cdfRecord.setValueByName(MCA10.ElapsedLiveTime(0), sedElapsedLiveTime);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recSedSpectrum(long sedSpectrum[]) throws ConverterException {
		if(mcaSpectrum[0] != null) {
			try {
				// Is this copy really required, this could be inefficient. //
				int nChannels = sedSpectrum.length;
				long tempSedSpectrum[] = new long[nChannels];
				System.arraycopy(sedSpectrum, 0, tempSedSpectrum, 0, nChannels);
				cdfRecord.setValueByName(MCA10.Spectrum(0), sedSpectrum);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	@Override
	public void initFedFastCount(int index, String name, String description) throws ConverterException {
		if(mcaFastCount[index] == null) {
			Variable v = createCdfVariable(MCA10.FastCount(index), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaFastCount[index] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initFedSlowCount(int index, String name, String description) throws ConverterException {
		if(mcaSlowCount[index] == null) {
			Variable v = createCdfVariable(MCA10.SlowCount(index), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaSlowCount[index] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initFedDeadTimePct(int index, String name, String description) throws ConverterException {
		if(mcaDeadTimePct[index] == null) {
			Variable v = createCdfVariable(MCA10.DeadTimePct(index), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.PERCENT);
			mcaDeadTimePct[index] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initFedElapsedRealTime(int index, String name, String description) throws ConverterException {
		if(mcaElapsedRealTime[index] == null) {
			Variable v = createCdfVariable(MCA10.ElapsedRealTime(index), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.SECOND);
			mcaElapsedRealTime[index] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initFedElapsedLiveTime(int index, String name, String description) throws ConverterException {
		if(mcaElapsedLiveTime[index] == null) {
			Variable v = createCdfVariable(MCA10.ElapsedLiveTime(index), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
			createMCAVarAttributes();
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.SECOND);
			mcaElapsedLiveTime[index] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initFedSpectrum(int index, int nChannels, String name, String description) throws ConverterException {
		if(mcaNElements == null) {
			mcaNElements = createCdfAttribute(MCA10.NElements(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mcaNElements, 0, CDF.CDF_INT2, 4);
			createMCACatEntry();
		}
		
		if(mcaSpectrum[index] == null) {
			Variable v = createCdfVariable(MCA10.Spectrum(index), CDF.CDF_UINT4, 1, 1, new long[] { nChannels });
			createMCAVarAttributes();
			if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
				try {
					v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
				}
				catch(CDFException e) {
					throw new ConverterException("CDF Set Variable Compression: Level: " + cdfSpectrumCompressionLevel, e);
				}
			}
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaSpectrum[index] = v;
			createMCACatEntry();
		}
	}

	@Override
	public void initFedSumSpectrum(int nChannels, String name, String description) throws ConverterException {
		if(mcaSumSpectrum == null) {
			Variable v = createCdfVariable(MCA10.SumSpectrum(), CDF.CDF_UINT4, 1, 1, new long[] { nChannels });
			createMCAVarAttributesWithCrts();
			if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
				try {
					v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
				}
				catch(CDFException e) {
					throw new ConverterException("CDF Set Variable Compression: Level: " + cdfSpectrumCompressionLevel, e);
				}
			}
			createCdfEntry(mcaCrts, v, CDF.CDF_CHAR, MCA10.DeadTimeCorrection);
			if(name != null) {
				createCdfEntry(mcaName, v, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaDesc, v, CDF.CDF_CHAR, description);
			}
			createCdfEntry(mcaUnit, v, CDF.CDF_CHAR, StdUnits.COUNT);
			mcaSumSpectrum = v;
			createMCACatEntry();
		}
	}

	@Override
	public void recFedFastCount(int index, long fedFastCount) throws ConverterException {
		if(mcaFastCount[index] != null) {
			try {
				cdfRecord.setValueByName(MCA10.FastCount(index), fedFastCount);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recFedSlowCount(int index, long fedSlowCount) throws ConverterException {
		if(mcaSlowCount[index] != null) {
			try {
				cdfRecord.setValueByName(MCA10.SlowCount(index), fedSlowCount);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recFedDeadTimePct(int index, double fedDeadTimePct) throws ConverterException {
		if(mcaDeadTimePct[index] != null) {
			try {
				cdfRecord.setValueByName(MCA10.DeadTimePct(index), fedDeadTimePct);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recFedElapsedRealTime(int index, double fedElapsedRealTime) throws ConverterException {
		if(mcaElapsedRealTime[index] != null) {
			try {
				cdfRecord.setValueByName(MCA10.ElapsedRealTime(index), fedElapsedRealTime);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recFedElapsedLiveTime(int index, double fedElapsedLiveTime) throws ConverterException {
		if(mcaElapsedLiveTime[index] != null) {
			try {
				cdfRecord.setValueByName(MCA10.ElapsedLiveTime(index), fedElapsedLiveTime);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}

	@Override
	public void recFedSpectrum(int index, long[] fedSpectrum) throws ConverterException {
		if(mcaSpectrum[index] != null) {
			try {
				// Is this copy really required, this could be inefficient. //
				int nChannels = fedSpectrum.length;
				long tempFedSpectrum[] = new long[nChannels];
				System.arraycopy(fedSpectrum, 0, tempFedSpectrum, 0, nChannels);
				cdfRecord.setValueByName(MCA10.Spectrum(index), tempFedSpectrum);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
	
	@Override
	public void recFedSumSpectrum(long[] fedSpectrum) throws ConverterException {
		if(mcaSumSpectrum != null) {
			try {
				// Is this copy really required, this could be inefficient. //
				int nChannels = fedSpectrum.length;
				long tempFedSpectrum[] = new long[nChannels];
				System.arraycopy(fedSpectrum, 0, tempFedSpectrum, 0, nChannels);
				cdfRecord.setValueByName(MCA10.SumSpectrum(), tempFedSpectrum);
			}
			catch(RecordFormatException e) {
				throw new ConverterException(e);
			}
		}
	}
		
	@Override
	public void initSedMaxEnergy(String name, String description) throws ConverterException {
		if(mcaMaxEnergy == null) {
			mcaMaxEnergy = createCdfAttribute(MCA10.MaxEnergy(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mcaMaxEnergy, 0, CDF.CDF_DOUBLE, 0.0);
			createCdfEntry(mcaMaxEnergy, 1, CDF.CDF_CHAR, StdUnits.EVOLT);
			if(name != null) {
				createCdfEntry(mcaMaxEnergy, 2, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaMaxEnergy, 3, CDF.CDF_CHAR, description);
			}
			createMCACatEntry();
		}
	}

	@Override
	public void initSedEnergyGapTime(String name, String description) throws ConverterException {
		if(mcaEnergyGapTime == null) { 
			mcaEnergyGapTime = createCdfAttribute(MCA10.EnergyGapTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mcaEnergyGapTime, 0, CDF.CDF_DOUBLE, 0.0);
			createCdfEntry(mcaEnergyGapTime, 1, CDF.CDF_CHAR, StdUnits.MICRO_SECOND);
			if(name != null) {
				createCdfEntry(mcaEnergyGapTime, 2, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaEnergyGapTime, 3, CDF.CDF_CHAR, description);
			}
			createMCACatEntry();
		}
	}

	@Override
	public void initSedEnergyPeakingTime(String name, String description) throws ConverterException {
		if(mcaEnergyPeakingTime == null) {
			mcaEnergyPeakingTime = createCdfAttribute(MCA10.EnergyPeakingTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mcaEnergyPeakingTime, 0, CDF.CDF_DOUBLE, 0.0);
			createCdfEntry(mcaEnergyPeakingTime, 1, CDF.CDF_CHAR, StdUnits.MICRO_SECOND);
			if(name != null) {
				createCdfEntry(mcaEnergyPeakingTime, 2, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaEnergyPeakingTime, 3, CDF.CDF_CHAR, description);
			}
			createMCACatEntry();
		}
	}

	@Override
	public void initSedPresetRealTime(String name, String description) throws ConverterException {
		if(mcaPresetRealTime == null) {
			mcaPresetRealTime = createCdfAttribute(MCA10.PresetRealTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(mcaPresetRealTime, 0, CDF.CDF_DOUBLE, 0.0);
			createCdfEntry(mcaPresetRealTime, 1, CDF.CDF_CHAR, StdUnits.SECOND);
			if(name != null) {
				createCdfEntry(mcaPresetRealTime, 2, CDF.CDF_CHAR, name);
			}
			if(description != null) {
				createCdfEntry(mcaPresetRealTime, 3, CDF.CDF_CHAR, description);
			}
			createMCACatEntry();
		}
	}
	
	@Override
	public void recSedMaxEnergy(double sedMaxEnergy) throws ConverterException {
		if(mcaMaxEnergy != null) {
			updateCdfEntry(mcaMaxEnergy, 0, CDF.CDF_DOUBLE, sedMaxEnergy);
		}
	}

	@Override
	public void recSedEnergyGapTime(double sedEnergyGapTime) throws ConverterException {
		if(mcaEnergyGapTime != null) {
			updateCdfEntry(mcaEnergyGapTime, 0, CDF.CDF_DOUBLE, sedEnergyGapTime);
		}
	}
	
	@Override
	public void recSedEnergyPeakingTime(double sedEnergyPeakingTime) throws ConverterException {
		if(mcaEnergyPeakingTime != null) {
			updateCdfEntry(mcaEnergyPeakingTime, 0, CDF.CDF_DOUBLE, sedEnergyPeakingTime);
		}
	}

	@Override
	public void recSedPresetRealTime(double sedPresetRealTime) throws ConverterException {
		if(mcaPresetRealTime != null) {
			updateCdfEntry(mcaPresetRealTime, 0, CDF.CDF_DOUBLE, sedPresetRealTime);
		}
	}
	
	@Override
	public void initFedMaxEnergy(String name, String description) throws ConverterException {
		initSedMaxEnergy(name, description);
	}

	@Override
	public void initFedEnergyGapTime(String name, String description) throws ConverterException {
		initSedEnergyGapTime(name, description);
	}

	@Override
	public void initFedEnergyPeakingTime(String name, String description) throws ConverterException {
		initSedEnergyPeakingTime(name, description);
	}

	@Override
	public void initFedPresetRealTime(String name, String description) throws ConverterException {
		initSedPresetRealTime(name, description);
	}

	@Override
	public void recFedMaxEnergy(double fedMaxEnergy) throws ConverterException {
		recSedMaxEnergy(fedMaxEnergy);
	}

	@Override
	public void recFedEnergyGapTime(double fedEnergyGapTime) throws ConverterException {
		recSedEnergyGapTime(fedEnergyGapTime);
	}

	@Override
	public void recFedEnergyPeakingTime(double fedEnergyPeakingTime) throws ConverterException {
		recSedEnergyPeakingTime(fedEnergyPeakingTime);
	}

	@Override
	public void recFedPresetRealTime(double fedPresetRealTime) throws ConverterException {
		recSedPresetRealTime(fedPresetRealTime);
	}

	protected void createMCAVarAttributes() throws ConverterException {
		if(mcaName == null) {
			mcaName = createCdfAttribute(MCA10.Name(), CDF.VARIABLE_SCOPE);
		}
		
		if(mcaDesc == null) {
			mcaDesc = createCdfAttribute(MCA10.Desc(), CDF.VARIABLE_SCOPE);
		}
		
		if(mcaUnit == null) {
			mcaUnit = createCdfAttribute(MCA10.Unit(), CDF.VARIABLE_SCOPE);
		}
	}
	
	protected void createMCAVarAttributesWithCrts() throws ConverterException {
		createMCAVarAttributes();
		
		if(mcaCrts == null) {
			mcaCrts = createCdfAttribute(MCA10.SumSpectrumCrts(), CDF.VARIABLE_SCOPE);
		}
	}
	
	protected void createMCACatEntry() throws ConverterException {
		if(mcaCatEntry == null) {
			if(mcaSumSpectrum != null) {
				mcaCatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, MCA10.getIdentity());
			}
			else {
				for(Variable v : mcaSpectrum) {
					if(v != null) {
						mcaCatEntry = createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, MCA10.getIdentity());
					}
				}
			}
		}
	}
	
	protected Attribute createCdfAttribute(String name, long scope) throws ConverterException {
		try {
			return Attribute.create(cdf, name, scope);
		}
		catch(CDFException e) {
			throw new ConverterException("Create Attribute: Name: " + name + ", Scope: " + scope, e);
		}
	}
	
	protected Variable createCdfVariable(String name, long dataType, long numElements, long numDims, long[] dimSizes) throws ConverterException {
		try {
			long[] dimVarys = new long[(int)numDims]; Arrays.fill(dimVarys, CDF.VARY);
			return Variable.create(cdf, name, dataType, numElements, numDims, dimSizes, CDF.VARY, dimVarys);
		}
		catch(CDFException e) {
			throw new ConverterException("CDF Create Variable: Name: " + name + ", Type: " + dataType + ", NElm: " + numElements + ", NDim: " + numDims, e);
		}
	}
	
	protected Entry createCdfEntry(Attribute attr, Variable var, long dataType, Object data) throws ConverterException {
		return createCdfEntry(attr, var.getID(), dataType, data);
	}
	protected Entry createCdfEntry(Attribute attr, long id, long dataType, Object data) throws ConverterException {
		try {
			return Entry.create(attr, id, dataType, data);
		}
		catch(CDFException e) {
			throw new ConverterException("CDF Create Entry: Attr: " + attr + ", ID: " + id + ", Type: " + dataType, e);
		}
	}
	protected void updateCdfEntry(Attribute attr, long id, long dataType, Object data) throws ConverterException {
		try {
			attr.getEntry(id).putData(dataType, data);
		}
		catch(CDFException e) {
			throw new ConverterException("CDF Create Entry: Attr: " + attr + ", ID: " + id + ", Type: " + dataType, e);
		}
	}
	
	public File getDataFile() {
		return dataFile;
	}
	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public File getSpectraFile() {
		return spectraFile;
	}
	public void setSpectraFile(File spectraFile) {
		this.spectraFile = spectraFile;
	}

	public File getCdfDataFile() {
		return cdfDataFile;
	}
	public void setCdfDataFile(File cdfDataFile) {
		this.cdfDataFile = cdfDataFile;
	}
	
	public boolean isForceUpdate() {
		return forceUpdate;
	}
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public long[] getCdfSpectrumCompressionLevel() {
		return cdfSpectrumCompressionLevel;
	}
	public void setCdfSpectrumCompressionLevel(long cdfSpectrumCompressionLevel) {
		this.cdfSpectrumCompressionLevel[0] = Math.min(Math.max(cdfSpectrumCompressionLevel, 
												CDF_MIN_COMPRESSION_LEVEL), CDF_MAX_COMPRESSION_LEVEL);
	}
}
