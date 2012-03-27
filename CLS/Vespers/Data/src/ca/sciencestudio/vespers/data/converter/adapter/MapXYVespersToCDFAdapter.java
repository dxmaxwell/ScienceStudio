/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersToCDFConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import gsfc.nssdc.cdf.Attribute;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Entry;
import gsfc.nssdc.cdf.Variable;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ca.sciencestudio.data.cdf.CDFEvent;
import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.CDFTypesUtility;
import ca.sciencestudio.data.daf.DAFDataParser;
import ca.sciencestudio.data.daf.DAFElement;
import ca.sciencestudio.data.daf.DAFRecord;
import ca.sciencestudio.data.daf.DAFSpectrumParser;
import ca.sciencestudio.data.standard.StdUnits;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 * 
 *
 */
public class MapXYVespersToCDFConverter extends AbstractMapXYVespersConverter {

	public static final long DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL = 6;
	
	private static final long[] EMPTY_LONG_ARRAY = new long[0];
	
	private static final long CDF_MAX_COMPRESSION_LEVEL = 9;
	private static final long CDF_MIN_COMPRESSION_LEVEL = 0;
	
	/// Parameters to be configured by a ConverterFactory ///
	private File cdfDataFile = null;
	
	private long[] cdfSpectrumCompressionLevel = new long[] { DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL };
	//////////////////////////////////////////////////////////
	
	private CDF cdf;
	
	private CDFEvent cdfEvent = null;
	private CDFRecord cdfRecord = null;
	
	private Map<String,Variable> cdfVariableMap = new LinkedHashMap<String,Variable>();
	private Map<String,Attribute> cdfAttributeMap = new LinkedHashMap<String,Attribute>();
	
	public MapXYVespersToCDFConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}

	@Override
	protected boolean openDestination(DAFDataParser dafDataParser, DAFSpectrumParser dafSpectrumParser) throws ConverterException {
		
		if(cdfDataFile.exists()) {
			log.debug("CDF file already exists in data directory.");
			boolean cdfDelete = false;
			
			if(isForceUpdate() || (cdfDataFile.length() == 0L)) {
				log.debug("Force update or existing file length is zero.");
				cdfDelete = true;
			}
			else if(cdfDataFile.lastModified() < getDafDataFile().lastModified()) {
				log.debug("DAF data file is newer than CDF data file.");
				cdfDelete = true;
			}
			else if(hasSingleElementDetector() || hasFourElementDetector()) {
				if(cdfDataFile.lastModified() < getDafSpectraFile().lastModified()) {
					log.debug("DAF spectra file is newer than CDF data file.");
					cdfDelete = true;
				}
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
		
		try {
			createAttributes(dafDataParser);
		}
		catch(CDFException e) {
			throw new ConverterException();
		}
		
		return false;
	}
	
	private void createAttributes(DAFDataParser dafDataParser) throws CDFException {

		Attribute catRegAttribute = createCdfAttribute(CATEGORY_REGISTRY, CDF.GLOBAL_SCOPE);
		
		Attribute a;
		
		int catRegEntryId = 0;

		// Category SS10 //
		
		createCdfEntry(catRegAttribute, catRegEntryId, CDF.CDF_CHAR, SS10.getIdentity());
		
		a = createCdfAttribute(SS10.Created(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch());
		
		a = createCdfAttribute(SS10.CreatedBy(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_CHAR, getClass().getName());
		
		a = createCdfAttribute(SS10.Source(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_CHAR, getDafDataFile().toString());
		if (hasSingleElementDetector() || hasFourElementDetector()) {
			createCdfEntry(a, 1, CDF.CDF_CHAR, getDafSpectraFile().toString());
		}
		
		// Category Scan10 // 
		
		Date epoch = new Date(0L);
		
		createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, Scan10.getIdentity());
		
		Date startTime = epoch;
		a = createCdfAttribute(Scan10.StartTime(), CDF.GLOBAL_SCOPE);
		if((getScanStartDate() != null) && (getScanStartDate().after(epoch))) {
			startTime = getScanStartDate();
		}
		else if((dafDataParser.getStartTime() != null) && (dafDataParser.getStartTime().after(epoch))) {
			startTime = dafDataParser.getStartTime();
		}
		else if((getDafDataFile() != null) && (getDafDataFile().lastModified() > epoch.getTime())) {
			startTime = new Date(getDafDataFile().lastModified());
		}
		createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(startTime));

		Date endTime = startTime;
		a = createCdfAttribute(Scan10.EndTime(), CDF.GLOBAL_SCOPE);
		if((getScanEndDate() != null) && (getScanStartDate().after(epoch))) {
			endTime = getScanEndDate();
		}
		createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(endTime));

		// Category MapXY11 //
		
		createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, MapXY11.getIdentity());

		a = createCdfAttribute(MapXY11.SizeX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_INT2, 0 /*sizeX*/);
		
		a = createCdfAttribute(MapXY11.SizeY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_INT2, 0 /*sizeY*/);
		
		a = createCdfAttribute(MapXY11.StartX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, 0.0 /*startX*/);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.EndX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, 0.0 /*endX*/);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.StepX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, 0.0 /*stepX*/);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.StartY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, 0.0 /*startY*/);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.EndY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, 0.0 /*endY*/);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.StepY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, 0.0 /*stepY*/);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);
		
		// Category SSModel10 //
		
		if (hasSSModel10()) {
			createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, SSModel10.getIdentity());
			
			a = createCdfAttribute(SSModel10.ProjectName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getProjectName());

			a = createCdfAttribute(SSModel10.SessionName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getSessionName());

			a = createCdfAttribute(SSModel10.Facility(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getFacilityName());

			a = createCdfAttribute(SSModel10.Laboratory(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getLaboratoryName());;
			
			a = createCdfAttribute(SSModel10.ExperimentName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getExperimentName());
			
			a = createCdfAttribute(SSModel10.Instrument(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getInstrumentName());
			
			a = createCdfAttribute(SSModel10.Technique(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getTechniqueName());

			a = createCdfAttribute(SSModel10.ScanName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getScanName());

			a = createCdfAttribute(SSModel10.SampleName(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_CHAR, getSampleName());
		}

		// Category BeamI11 //
		
		if(hasBeamCurrent()) {
			createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, BeamI11.getIdentity());
		}

		// Category MapXY11 //
		
		if (hasSingleElementDetector() || hasFourElementDetector()) {
			createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, MCA10.getIdentity());
			
			a = createCdfAttribute(MCA10.NElements(), CDF.GLOBAL_SCOPE);
			if(hasSingleElementDetector()) {
				createCdfEntry(a, 0, CDF.CDF_INT2, 1);
			}
			else if(hasFourElementDetector()) {
				if(allGreaterThanOrEqual(getFedSpectrumIdx(), 0)) {
					createCdfEntry(a, 0, CDF.CDF_INT2, 4);
				} else {
					createCdfEntry(a, 0, CDF.CDF_INT2, 0);
				}
			}
		}
	}
	
	@Override
	protected void closeDestination() throws ConverterException {
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
	protected void deleteDestination() throws ConverterException {
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
	protected void openDataRecord(int dataRecordIndex, DAFRecord record, int sedNChannels, int fedNChannels) throws ConverterException {
		
		if(dataRecordIndex == 0) {
			try {
				createDataVariables(record, sedNChannels, fedNChannels);
			}
			catch(CDFException e) {
				throw new ConverterException("Error while creating CDF Variables.", e);
			}
		}
		
		cdfRecord = new CDFRecord(cdfEvent);
	}
	
	private void createDataVariables(DAFRecord record, int sedNChannels, int fedNChannels) throws CDFException {	
		
		Variable v;
		
		List<DAFElement> dataEventElements = record.getEvent().getElements();

		// Category MapXY11 //
				
		createCdfAttribute(MapXY11.Name(), CDF.VARIABLE_SCOPE);
		createCdfAttribute(MapXY11.Desc(), CDF.VARIABLE_SCOPE);
		createCdfAttribute(MapXY11.Unit(), CDF.VARIABLE_SCOPE);
		
		v = createCdfVariable(MapXY11.I(), CDF.CDF_INT2, 1, 0, EMPTY_LONG_ARRAY);
		createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, "Computed point index in X-direction.");
		
		v = createCdfVariable(MapXY11.J(), CDF.CDF_INT2, 1, 0, EMPTY_LONG_ARRAY);
		createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, "Computed point index in Y-direction.");
		
		v = createCdfVariable(MapXY11.X(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
		if(getPosXFeedbackIdx() >= 0) {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getPosXFeedbackIdx()).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getPosXFeedbackIdx()).getDescription());
		}
		else {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getPosXSetpointIdx()).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getPosXFeedbackIdx()).getDescription());
		}
		createCdfEntry(MapXY11.Unit(), v, CDF.CDF_CHAR, StdUnits.MILLI_METER);
		
		v = createCdfVariable(MapXY11.Y(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
		if(getPosYFeedbackIdx() >= 0) {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getPosYFeedbackIdx()).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getPosYFeedbackIdx()).getDescription());
		}
		else {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getPosYSetpointIdx()).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getPosYFeedbackIdx()).getDescription());
		}
		createCdfEntry(MapXY11.Unit(), v, CDF.CDF_CHAR, StdUnits.MILLI_METER);
		
		// BeamI11 Category //
		
		if(hasBeamCurrent()) {
			
			createCdfAttribute(BeamI11.Name(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(BeamI11.Desc(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(BeamI11.Unit(), CDF.VARIABLE_SCOPE);
			
			if(getSrCurrentIdx() >= 0) {
				v = createCdfVariable(BeamI11.SR(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(BeamI11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSrCurrentIdx()).getName());
				createCdfEntry(BeamI11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSrCurrentIdx()).getDescription());
				createCdfEntry(BeamI11.Unit(), v, CDF.CDF_CHAR, StdUnits.MILLI_AMPERE);
			}
			
			for(int i=0; i<getMcsCurrentIdx().length; i++) {
				if(getMcsCurrentIdx()[i] >= 0) {
					v = createCdfVariable(BeamI11.I(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(BeamI11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getMcsCurrentIdx()[i]).getName());
					createCdfEntry(BeamI11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getMcsCurrentIdx()[i]).getDescription());
					createCdfEntry(BeamI11.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}	
		}
		
		// MCA10 Category //
		
		if(hasSingleElementDetector()) {
			
			if(sedNChannels < 0) {
				sedNChannels = getSedDefaultNChannels();
			}
			
			createCdfAttribute(MCA10.Name(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Desc(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Unit(), CDF.VARIABLE_SCOPE);
						
			if(getSedFastCountIdx() >= 0) {
				v = createCdfVariable(MCA10.FastCount(0), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSedFastCountIdx()).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSedFastCountIdx()).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
			}
			
			if(getSedSlowCountIdx() >= 0) {
				v = createCdfVariable(MCA10.SlowCount(0), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSedSlowCountIdx()).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSedSlowCountIdx()).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
			}
			
			if(getSedDeadTimePctIdx() >= 0) {  
				v = createCdfVariable(MCA10.DeadTimePct(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSedDeadTimePctIdx()).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSedDeadTimePctIdx()).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.PERCENT);
			}
			
			if(getSedElapsedRealTimeIdx() >= 0) {
				v = createCdfVariable(MCA10.ElapsedRealTime(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSedElapsedRealTimeIdx()).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSedElapsedRealTimeIdx()).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
			}
			
			if(getSedElpasedLiveTimeIdx() >= 0) {
				v = createCdfVariable(MCA10.ElapsedLiveTime(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSedElpasedLiveTimeIdx()).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSedElpasedLiveTimeIdx()).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
			}
			
			v = createCdfVariable(MCA10.Spectrum(0), CDF.CDF_UINT4, 1, 1, new long[] { sedNChannels });
			if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
				v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
			}
			createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getSedSpectrumIdx()).getName());
			createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getSedSpectrumIdx()).getDescription());
			createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
		}
		else if(hasFourElementDetector()) {
			
			if(fedNChannels < 0) {
				fedNChannels = getFedDefaultNChannels();
			}
			
			createCdfAttribute(MCA10.Name(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Desc(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Unit(), CDF.VARIABLE_SCOPE);
			
			if(allGreaterThanOrEqual(getFedFastCountIdx(), 0)) {
				for(int i=0; i<getFedFastCountIdx().length; i++) {
					v = createCdfVariable(MCA10.FastCount(i), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedFastCountIdx()[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedFastCountIdx()[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}
			
			if(allGreaterThanOrEqual(getFedSlowCountIdx(), 0)) {
				for(int i=0; i<getFedSlowCountIdx().length; i++) {
					v = createCdfVariable(MCA10.SlowCount(i), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedSlowCountIdx()[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedSlowCountIdx()[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}
			
			if(allGreaterThanOrEqual(getFedDeadTimePctIdx(), 0)) {
				for(int i=0; i<getFedDeadTimePctIdx().length; i++) {
					v = createCdfVariable(MCA10.DeadTimePct(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedDeadTimePctIdx()[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedDeadTimePctIdx()[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.PERCENT);
				}
			}
			
			if(allGreaterThanOrEqual(getFedElapsedRealTimeIdx(), 0)) {
				for(int i=0; i<getFedElapsedRealTimeIdx().length; i++) {
					v = createCdfVariable(MCA10.ElapsedRealTime(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedElapsedRealTimeIdx()[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedElapsedRealTimeIdx()[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
				}
			}

			if(allGreaterThanOrEqual(getFedElpasedLiveTimeIdx(), 0)) {
				for(int i=0; i<getFedElpasedLiveTimeIdx().length; i++) {
					v = createCdfVariable(MCA10.ElapsedLiveTime(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedElpasedLiveTimeIdx()[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedElpasedLiveTimeIdx()[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
				}
			}
			
			if(getFedSumSpectrumIdx() >= 0) {
				createCdfAttribute(MCA10.SumSpectrumCrts(), CDF.VARIABLE_SCOPE);	
				v = createCdfVariable(MCA10.SumSpectrum(), CDF.CDF_UINT4, 1, 1, new long[] { fedNChannels });
				if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
					v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
				}
				createCdfEntry(MCA10.SumSpectrumCrts(),  v, CDF.CDF_CHAR, MCA10.DeadTimeCorrection);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedSumSpectrumIdx()).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedSumSpectrumIdx()).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
			}
			
			if(allGreaterThanOrEqual(getFedSpectrumIdx(), 0)) {
				for(int i=0; i<getFedSpectrumIdx().length; i++) {
					v = createCdfVariable(MCA10.Spectrum(i), CDF.CDF_UINT4, 1, 1, new long[] { fedNChannels });
					if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
						v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
					}
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(getFedSpectrumIdx()[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(getFedSpectrumIdx()[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}
		}
		
		cdfEvent = new CDFEvent(getCdfVariables());
	}
	
	@Override
	protected void closeDataRecord(int dataRecordIndex) throws ConverterException {
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
	protected void recStepX(double stepX) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.StepX());
		try {
			a.getEntry(0).putData(CDF.CDF_DOUBLE, stepX);
		}
		catch(Exception e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSizeX(int sizeX) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.SizeX());
		try {
			a.getEntry(0).putData(CDF.CDF_UINT2, sizeX);
		}
		catch(Exception e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recStartX(double startX) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.StartX());
		try {
			a.getEntry(0).putData(CDF.CDF_DOUBLE, startX);
		}
		catch(Exception e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recEndX(double endX) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.EndX());
		try {
			a.getEntry(0).putData(CDF.CDF_DOUBLE, endX);
		}
		catch(Exception e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recPointX(int pointX) throws ConverterException {
		try {
			cdfRecord.setValueByName(MapXY11.I(), pointX);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recPositionX(double positionX) throws ConverterException {
		try {
			cdfRecord.setValueByName(MapXY11.X(), positionX);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recStepY(double stepY) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.StepY());
		try {
			a.getEntry(0).putData(CDF.CDF_DOUBLE, stepY);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSizeY(int sizeY) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.SizeY());
		try {
			a.getEntry(0).putData(CDF.CDF_UINT2, sizeY);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recStartY(double startY) throws ConverterException {
		Attribute a = getCdfAttribute(MapXY11.StartY());
		try {
			a.getEntry(0).putData(CDF.CDF_DOUBLE, startY);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recEndY(double endY) throws ConverterException {		
		Attribute a = getCdfAttribute(MapXY11.EndY());
		try {
			a.getEntry(0).putData(CDF.CDF_DOUBLE, endY);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recPointY(int pointY) throws ConverterException {
		try {
			cdfRecord.setValueByName(MapXY11.J(), pointY);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recPositionY(double positionY) throws ConverterException {
		try {
			cdfRecord.setValueByName(MapXY11.Y(), positionY);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}	

	@Override
	protected void recSRCurrent(double srCurrent) throws ConverterException {
		try {
			cdfRecord.setValueByName(BeamI11.SR(), srCurrent);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recMCSCurrent(int index, double mcsCurrent) throws ConverterException {
		try {
			cdfRecord.setValueByName(BeamI11.I(index), mcsCurrent);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSedFastCount(long sedFastCount) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.FastCount(0), sedFastCount);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSedSlowCount(long sedSlowCount) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.SlowCount(0), sedSlowCount);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSedDeadTimePct(double sedDeadTimePct) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.DeadTimePct(0), sedDeadTimePct);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSedElapsedRealTime(double sedElapsedRealTime) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.ElapsedRealTime(0), sedElapsedRealTime);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recSedElapsedLiveTime(double sedElapsedLiveTime) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.ElapsedLiveTime(0), sedElapsedLiveTime);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recSedSpectrum(long sedSpectrum[]) throws ConverterException {
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

	@Override
	protected void recFedFastCount(int index, long fedFastCount) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.FastCount(index), fedFastCount);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedSlowCount(int index, long fedSlowCount) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.SlowCount(index), fedSlowCount);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedDeadTimePct(int index, double fedDeadTimePct) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.DeadTimePct(index), fedDeadTimePct);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedElapsedRealTime(int index, double fedElapsedRealTime) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.ElapsedRealTime(index), fedElapsedRealTime);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}	
	}

	@Override
	protected void recFedElapsedLiveTime(int index, double fedElapsedLiveTime) throws ConverterException {
		try {
			cdfRecord.setValueByName(MCA10.ElapsedLiveTime(index), fedElapsedLiveTime);
		}
		catch(RecordFormatException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedSpectrum(int index, long[] fedSpectrum) throws ConverterException {
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
	
	@Override
	protected void recFedSumSpectrum(long[] fedSpectrum) throws ConverterException {
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
	
	@Override
	protected void openBkgdRecord(int bkgdRecordIndex) {
		// nothing to do //
	}

	@Override
	protected void closeBkgdRecord(int bkgdRecordIndex) {
		// nothing to do //
	}

	@Override
	protected void recSedMaxEnergy(double sedMaxEnergy, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.MaxEnergy(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedMaxEnergy);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recSedEnergyGapTime(double sedEnergyGapTime, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.EnergyGapTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedEnergyGapTime);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recSedEnergyPeakingTime(double sedEnergyPeakingTime, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.EnergyPeakingTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedEnergyPeakingTime);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recSedPresetReadTime(double sedPresetRealTime, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.PresetRealTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedPresetRealTime);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	protected void recFedMaxEnergy(double fedMaxEnergy, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.MaxEnergy(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedMaxEnergy);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedEnergyGapTime(double fedEnergyGapTime, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.EnergyGapTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedEnergyGapTime);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedEnergyPeakingTime(double fedEnergyPeakingTime, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.EnergyPeakingTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedEnergyPeakingTime);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void recFedPresetRealTime(double fedPresetRealTime, String units, String name, String description) throws ConverterException {
		try {
			Attribute a = createCdfAttribute(MCA10.PresetRealTime(), CDF.GLOBAL_SCOPE);
			createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedPresetRealTime);
			createCdfEntry(a, 1, CDF.CDF_CHAR, units);
			createCdfEntry(a, 2, CDF.CDF_CHAR, name);
			createCdfEntry(a, 3, CDF.CDF_CHAR, description);
		}
		catch(CDFException e) {
			throw new ConverterException(e);
		}
	}

	@Override
	protected void openPadRecord(int dataRecordIndex) throws ConverterException {
		
		
	}
	
	protected void closePadRecord(int dataRecordIndex) throws ConverterException {
		try {
			cdf.putRecord(dataRecordIndex, cdfRecord.getEvent().getVariableIDs(), cdfRecord.getCDFValues());
		}
		catch(Exception e) {
			throw new ConverterException(e);
		}
	}
	
	
	protected Attribute createCdfAttribute(String name, long scope) throws CDFException {
		Attribute a  = Attribute.create(cdf, name, scope);
		cdfAttributeMap.put(name, a);
		return a;
	}
	protected Attribute getCdfAttribute(String name) {
		return cdfAttributeMap.get(name);
	}
	protected Collection<Attribute> getCdfAttributes() {
		return cdfAttributeMap.values();
	}
	
	protected Variable createCdfVariable(String name, long dataType, long numElements, long numDims, long[] dimSizes) throws CDFException {
		long recVary = CDF.VARY;
		long[] dimVarys = new long[(int)numDims];
		Arrays.fill(dimVarys, recVary);
		Variable variable = Variable.create(cdf, name, dataType, numElements, numDims, dimSizes, recVary, dimVarys);
		cdfVariableMap.put(name, variable);
		return variable;
	}
	protected Variable getCdfVariable(String name) {
		return cdfVariableMap.get(name);
	}
	protected Collection<Variable> getCdfVariables() {
		return cdfVariableMap.values();
	}
	
	protected Entry createCdfEntry(String attrName, String varName, long dataType, Object data) throws CDFException {
		return createCdfEntry(attrName, getCdfVariable(varName).getID(), dataType, data);
	}
	protected Entry createCdfEntry(String attrName, Variable var, long dataType, Object data) throws CDFException {
		return createCdfEntry(attrName, var.getID(), dataType, data);
	}
	protected Entry createCdfEntry(String attrName, long id, long dataType, Object data) throws CDFException {
		return createCdfEntry(getCdfAttribute(attrName), id, dataType, data);
	}
	protected Entry createCdfEntry(Attribute attr, Variable var, long dataType, Object data) throws CDFException {
		return createCdfEntry(attr, var.getID(), dataType, data);
	}
	protected Entry createCdfEntry(Attribute attr, long id, long dataType, Object data) throws CDFException {
		return Entry.create(attr, id, dataType, data);
	}
	
	public void setCdfDataFile(File cdfDataFile) {
		this.cdfDataFile = cdfDataFile;
	}
	
	public void setCdfSpectrumCompressionLevel(long cdfSpectrumCompressionLevel) {
		this.cdfSpectrumCompressionLevel[0] = Math.min(Math.max(cdfSpectrumCompressionLevel, 
												CDF_MIN_COMPRESSION_LEVEL), CDF_MAX_COMPRESSION_LEVEL);
	}
}
