/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapXYVespersConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import gsfc.nssdc.cdf.Attribute;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Entry;
import gsfc.nssdc.cdf.Variable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.cdf.CDFEvent;
import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.CDFTypesUtility;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.daf.DAFDataParser;
import ca.sciencestudio.data.daf.DAFElement;
import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.daf.DAFRecord;
import ca.sciencestudio.data.daf.DAFRecordParser;
import ca.sciencestudio.data.daf.DAFSpectrumParser;
import ca.sciencestudio.data.daf.DAFSpectrumRecord;
import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.standard.StdUnits;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.data.support.DataFormatException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 * 
 */
public abstract class AbstractMapXYVespersConverter extends AbstractVespersConverter implements StdCategories {

	public static final int DEFAULT_SED_NCHANNELS = 2048;
	public static final int DEFAULT_FED_NCHANNELS = 2048;
	public static final long DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL = 6;

	private static final long[] EMPTY_LONG_ARRAY = new long[0];

	private static final double MAXIMUM_STEP_ERROR = 0.001;
	
	private static final long CDF_MAX_COMPRESSION_LEVEL = 9;
	private static final long CDF_MIN_COMPRESSION_LEVEL = 0;
	
	//private static final String FOUR_ELEMENT_DETECTOR_UID = "FED";
	//private static final String SINGLE_ELEMENT_DETECTOR_UID = "SED";

	/// Parameters to be configured by a ConverterFactory ///
	private File cdfDataFile = null;
	private File dafDataFile = null;
	private File dafSpectraFile = null;

	private int dataEventId = -1;
	private int bkgdEventId = -1;

	private int posXSetpointIdx = -1;
	private int posYSetpointIdx = -1;
	
	private int posXFeedbackIdx = -1;
	private int posYFeedbackIdx = -1;
	
	// Parameters for Beam Current. //
	private int srCurrentIdx = -1;
	private int[] mcsCurrentIdx = { -1, -1, -1, -1, -1, -1 };
	
	// Parameters for the Single Element Detector. //
	private int sedNChannelsIdx = -1;
	private int sedMaxEnergyIdx = -1;
	private int sedEnergyGapTimeIdx = -1;
	private int sedPresetRealTimeIdx = -1;
	private int sedEnergyPeakingTimeIdx = -1;	

	private int sedSpectrumIdx = -1;
	private int sedFastCountIdx = -1;
	private int sedSlowCountIdx = -1;
	private int sedDeadTimePctIdx = -1;
	private int sedElapsedRealTimeIdx = -1;
	private int sedElpasedLiveTimeIdx = -1;
	
	private int sedDefaultNChannels = DEFAULT_SED_NCHANNELS;

	// Parameters for the Four Element Detector. //
	private int fedNChannelsIdx = -1;
	private int fedMaxEnergyIdx = -1;
	private int fedEnergyGapTimeIdx = -1;
	private int fedPresetRealTimeIdx = -1;
	private int fedEnergyPeakingTimeIdx = -1;
	
	private int fedSumSpectrumIdx = -1;
	private int[] fedSpectrumIdx = { -1, -1, -1, -1 };
	private int[] fedFastCountIdx = { -1, -1, -1, -1 };
	private int[] fedSlowCountIdx = { -1, -1, -1, -1 };
	private int[] fedDeadTimePctIdx = { -1, -1, -1, -1 };
	private int[] fedElapsedRealTimeIdx = { -1, -1, -1, -1 };
	private int[] fedElpasedLiveTimeIdx = { -1, -1, -1, -1 };

	private int fedDefaultNChannels = DEFAULT_FED_NCHANNELS;
	
	private Collection<DAFRecordParser> customRecordParsers = null;
	
	private long[] cdfSpectrumCompressionLevel = new long[] { DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL };
	//////////////////////////////////////////////////////////

	private CDF cdf;

	private int dataRecordIndex = -1;
	private int bkgdRecordIndex = -1;

	private DAFDataParser dafDataParser;
	private DAFSpectrumParser dafSpectraParser;

	private CDFEvent cdfEvent = null;
	private CDFRecord cdfRecord = null;
	
	private int sizeX = 1, sizeY = 1;
	private double stepX = 0.0, stepY = 0.0;
	private double startX = 0.0, startY = 0.0;
	private double endX = 0.0, endY = 0.0;
	
	private int pointX = 0, pointY = 0;
	private double posX = 0.0, posY = 0.0;
	private double nextX = 0.0, nextY = 0.0;
	private double prevX = 0.0, prevY = 0.0;
	private boolean firstX = true, firstY = true;

	private int sedNChannels = -1;
	private long[] sedSpectrum = null;
	
	private int fedNChannels = -1;
	private long[][] fedSpectrum = null;
	private long[] fedSumSpectrum = null;
	
	private Map<String,Variable> cdfVariableMap = new LinkedHashMap<String,Variable>();
	private Map<String,Attribute> cdfAttributeMap = new LinkedHashMap<String,Attribute>();
	
	protected Log log = LogFactory.getLog(getClass());

	public AbstractMapXYVespersConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}

	public ConverterMap convert() throws ConverterException {

		if(!hasPositionXY()) {
			throw new ConverterException("Data file does not contain required X and Y positions.");
		}

		try {
			dafDataParser = new DAFDataParser(dafDataFile, customRecordParsers);
		}
		catch(Exception e) {
			throw new ConverterException("Exception while creating data parser for file: " + dafDataFile, e);
		}

		if(dataEventId >= 0) {
			DAFEvent dataEvent = dafDataParser.getEventById(dataEventId);
			if(dataEvent == null) {
				throw new ConverterException("Data event with id," + dataEventId + ", not found.");
			}
		}
		
		if(bkgdEventId >= 0) {
			DAFEvent bkgdEvent = dafDataParser.getEventById(bkgdEventId);
			if(bkgdEvent == null) {
				throw new ConverterException("Background event with id, " + bkgdEventId + ", not found.");
			}
		}

		if(hasSingleElementDetector() || hasFourElementDetector()) {
			try {
				dafSpectraParser = new DAFSpectrumParser(dafSpectraFile);
			}
			catch(Exception e) {
				throw new ConverterException("Exception while creating spectra parser for file: " + dafSpectraFile, e);
			}
		}

		if(cdfDataFile.exists()) {
			log.debug("CDF file already exists in data directory.");
			boolean cdfDelete = false;
			
			if(isForceUpdate() || (cdfDataFile.length() == 0L)) {
				log.debug("Force update or existing file length is zero.");
				cdfDelete = true;
			}
			else if(cdfDataFile.lastModified() < dafDataFile.lastModified()) {
				log.debug("DAF data file is newer than CDF data file.");
				cdfDelete = true;
			}
			else if(hasSingleElementDetector()) {
				if(cdfDataFile.lastModified() < dafSpectraFile.lastModified()) {
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
				return getResponse();
			}
		}
		
		try {
			cdf = CDF.create(cdfDataFile.getAbsolutePath());
		}
		catch(CDFException e) {
			throw new ConverterException("Exception while creating CDF file: " + cdfDataFile, e);
		}

		try {
			createAttributes();

			for (DAFRecord record : dafDataParser) {
				if (record.getEvent().getId() == dataEventId) {
					dataRecordIndex++;
					processDataRecord(record);
				}
				else if (record.getEvent().getId() == bkgdEventId) {
					bkgdRecordIndex++;
					processBkgdRecord(record);
				}
				else {
					// nothing to do        //
					// Maybe Log something. //
				}
			}

			padMissingPoints();
			
			cdf.close();
		}
		catch(CDFException e) {
			cdfDataFile.delete();
			throw new ConverterException("While parsing data file, CDF exception.", e);
		}
		catch(ConverterException e) {
			cdfDataFile.delete();
			throw e;
		}
		
		return getResponse();
	}

	protected boolean hasPositionXY() {
		return (dafDataFile != null) && (dataEventId >= 0) 
					&& (posXFeedbackIdx >= 0) && (posYFeedbackIdx >= 0);
	}

	protected boolean hasBeamCurrent() {
		return (dafDataFile != null) && (dataEventId >= 0) &&
					((srCurrentIdx >= 0) || oneGreaterThanOrEqual(mcsCurrentIdx, 0)); 
	}
	
	protected boolean hasSingleElementDetector() {
		return (dafDataFile != null) && (dafSpectraFile != null) 
					&& (dataEventId >= 0) && (sedSpectrumIdx >= 0);
	}

	protected boolean hasFourElementDetector() {
		return (dafDataFile != null) && (dafSpectraFile != null) 
					&& (dataEventId >= 0) && ((fedSumSpectrumIdx >= 0) || 
									allGreaterThanOrEqual(fedSpectrumIdx, 0));
	}
	
	protected void createAttributes() throws CDFException {

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
		createCdfEntry(a, 0, CDF.CDF_CHAR, dafDataFile.toString());
		if (hasSingleElementDetector() || hasFourElementDetector()) {
			createCdfEntry(a, 1, CDF.CDF_CHAR, dafSpectraFile.toString());
		}
		
		// Category Scan10 //
		
		createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, Scan10.getIdentity());
		
		a = createCdfAttribute(Scan10.StartTime(), CDF.GLOBAL_SCOPE);
		if(getScanStartDate() != null) {
			createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(getScanStartDate()));
		} else {
			createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(dafDataParser.getStartTime()));
		}

		a = createCdfAttribute(Scan10.EndTime(), CDF.GLOBAL_SCOPE);
		if(getScanEndDate() != null) {
			createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(getScanEndDate()));
		} else {
			createCdfEntry(a, 0, CDF.CDF_EPOCH, CDFTypesUtility.getCDFEpoch(dafDataParser.getStartTime()));
		}

		// Category MapXY11 //
		
		createCdfEntry(catRegAttribute, catRegEntryId++, CDF.CDF_CHAR, MapXY11.getIdentity());

		a = createCdfAttribute(MapXY11.SizeX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_INT2, sizeX);
		
		a = createCdfAttribute(MapXY11.SizeY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_INT2, sizeY);
		
		a = createCdfAttribute(MapXY11.StartX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, startX);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.EndX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, endX);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.StepX(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, stepX);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.StartY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, startY);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.EndY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, endY);
		createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MILLI_METER);

		a = createCdfAttribute(MapXY11.StepY(), CDF.GLOBAL_SCOPE);
		createCdfEntry(a, 0, CDF.CDF_DOUBLE, stepY);
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
				if(allGreaterThanOrEqual(fedSpectrumIdx, 0)) {
					createCdfEntry(a, 0, CDF.CDF_INT2, 4);
				} else {
					createCdfEntry(a, 0, CDF.CDF_INT2, 0);
				}
			}
		}
	}

	protected void createDataVariables(DAFRecord record) throws CDFException {	
		
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
		if(posXFeedbackIdx >= 0) {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(posXFeedbackIdx).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(posXFeedbackIdx).getDescription());
		}
		else {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(posXSetpointIdx).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(posXFeedbackIdx).getDescription());
		}
		createCdfEntry(MapXY11.Unit(), v, CDF.CDF_CHAR, StdUnits.MILLI_METER);
		
		v = createCdfVariable(MapXY11.Y(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
		if(posYFeedbackIdx >= 0) {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(posYFeedbackIdx).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(posYFeedbackIdx).getDescription());
		}
		else {
			createCdfEntry(MapXY11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(posYSetpointIdx).getName());
			createCdfEntry(MapXY11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(posYFeedbackIdx).getDescription());
		}
		createCdfEntry(MapXY11.Unit(), v, CDF.CDF_CHAR, StdUnits.MILLI_METER);
		
		// BeamI11 Category //
		
		if(hasBeamCurrent()) {
			
			createCdfAttribute(BeamI11.Name(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(BeamI11.Desc(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(BeamI11.Unit(), CDF.VARIABLE_SCOPE);
			
			if(srCurrentIdx >= 0) {
				v = createCdfVariable(BeamI11.SR(), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(BeamI11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(srCurrentIdx).getName());
				createCdfEntry(BeamI11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(srCurrentIdx).getDescription());
				createCdfEntry(BeamI11.Unit(), v, CDF.CDF_CHAR, StdUnits.MILLI_AMPERE);
			}
			
			for(int i=0; i<mcsCurrentIdx.length; i++) {
				if(mcsCurrentIdx[i] >= 0) {
					v = createCdfVariable(BeamI11.I(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(BeamI11.Name(), v, CDF.CDF_CHAR, dataEventElements.get(mcsCurrentIdx[i]).getName());
					createCdfEntry(BeamI11.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(mcsCurrentIdx[i]).getDescription());
					createCdfEntry(BeamI11.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}	
		}
		
		// MCA10 Category //
		
		if(hasSingleElementDetector()) {
			
			if(sedNChannels < 0) {
				sedNChannels = sedDefaultNChannels;
			}
			
			createCdfAttribute(MCA10.Name(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Desc(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Unit(), CDF.VARIABLE_SCOPE);
						
			if(sedFastCountIdx >= 0) {
				v = createCdfVariable(MCA10.FastCount(0), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(sedFastCountIdx).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(sedFastCountIdx).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
			}
			
			if(sedSlowCountIdx >= 0) {
				v = createCdfVariable(MCA10.SlowCount(0), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(sedSlowCountIdx).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(sedSlowCountIdx).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
			}
			
			if(sedDeadTimePctIdx >= 0) {  
				v = createCdfVariable(MCA10.DeadTimePct(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(sedDeadTimePctIdx).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(sedDeadTimePctIdx).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.PERCENT);
			}
			
			if(sedElapsedRealTimeIdx >= 0) {
				v = createCdfVariable(MCA10.ElapsedRealTime(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(sedElapsedRealTimeIdx).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(sedElapsedRealTimeIdx).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
			}
			
			if(sedElpasedLiveTimeIdx >= 0) {
				v = createCdfVariable(MCA10.ElapsedLiveTime(0), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(sedElpasedLiveTimeIdx).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR,dataEventElements.get(sedElpasedLiveTimeIdx).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
			}
			
			v = createCdfVariable(MCA10.Spectrum(0), CDF.CDF_UINT4, 1, 1, new long[] { sedNChannels });
			if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
				v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
			}
			createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(sedSpectrumIdx).getName());
			createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(sedSpectrumIdx).getDescription());
			createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
		}
		else if(hasFourElementDetector()) {
			
			if(fedNChannels < 0) {
				fedNChannels = fedDefaultNChannels;
			}
			
			createCdfAttribute(MCA10.Name(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Desc(), CDF.VARIABLE_SCOPE);
			createCdfAttribute(MCA10.Unit(), CDF.VARIABLE_SCOPE);
			
			if(allGreaterThanOrEqual(fedFastCountIdx, 0)) {
				for(int i=0; i<fedFastCountIdx.length; i++) {
					v = createCdfVariable(MCA10.FastCount(i), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedFastCountIdx[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedFastCountIdx[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}
			
			if(allGreaterThanOrEqual(fedSlowCountIdx, 0)) {
				for(int i=0; i<fedSlowCountIdx.length; i++) {
					v = createCdfVariable(MCA10.SlowCount(i), CDF.CDF_UINT4, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedSlowCountIdx[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedSlowCountIdx[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}
			
			if(allGreaterThanOrEqual(fedDeadTimePctIdx, 0)) {
				for(int i=0; i<fedDeadTimePctIdx.length; i++) {
					v = createCdfVariable(MCA10.DeadTimePct(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedDeadTimePctIdx[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedDeadTimePctIdx[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.PERCENT);
				}
			}
			
			if(allGreaterThanOrEqual(fedElapsedRealTimeIdx, 0)) {
				for(int i=0; i<fedElapsedRealTimeIdx.length; i++) {
					v = createCdfVariable(MCA10.ElapsedRealTime(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedElapsedRealTimeIdx[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedElapsedRealTimeIdx[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
				}
			}

			if(allGreaterThanOrEqual(fedElpasedLiveTimeIdx, 0)) {
				for(int i=0; i<fedElpasedLiveTimeIdx.length; i++) {
					v = createCdfVariable(MCA10.ElapsedLiveTime(i), CDF.CDF_DOUBLE, 1, 0, EMPTY_LONG_ARRAY);
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedElpasedLiveTimeIdx[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedElpasedLiveTimeIdx[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.SECOND);
				}
			}
			
			if(fedSumSpectrumIdx >= 0) {
				createCdfAttribute(MCA10.SumSpectrumCrts(), CDF.VARIABLE_SCOPE);	
				v = createCdfVariable(MCA10.SumSpectrum(), CDF.CDF_UINT4, 1, 1, new long[] { fedNChannels });
				if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
					v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
				}
				createCdfEntry(MCA10.SumSpectrumCrts(),  v, CDF.CDF_CHAR, MCA10.DeadTimeCorrection);
				createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedSumSpectrumIdx).getName());
				createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedSumSpectrumIdx).getDescription());
				createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
			}
			
			if(allGreaterThanOrEqual(fedSpectrumIdx, 0)) {
				for(int i=0; i<fedSpectrumIdx.length; i++) {
					v = createCdfVariable(MCA10.Spectrum(i), CDF.CDF_UINT4, 1, 1, new long[] { fedNChannels });
					if(cdfSpectrumCompressionLevel[0] > CDF_MIN_COMPRESSION_LEVEL) {
						v.setCompression(CDF.GZIP_COMPRESSION, cdfSpectrumCompressionLevel);
					}
					createCdfEntry(MCA10.Name(), v, CDF.CDF_CHAR, dataEventElements.get(fedSpectrumIdx[i]).getName());
					createCdfEntry(MCA10.Desc(), v, CDF.CDF_CHAR, dataEventElements.get(fedSpectrumIdx[i]).getDescription());
					createCdfEntry(MCA10.Unit(), v, CDF.CDF_CHAR, StdUnits.COUNT);
				}
			}
		}
		
		cdfEvent = new CDFEvent(getCdfVariables());
	}
	
	protected void processDataRecord(DAFRecord record) throws ConverterException, CDFException {
		
		Attribute a;
		
		if(dataRecordIndex == 0) {
			createDataVariables(record);
		}
		
		try {
			if(dataRecordIndex == 0) {
				prevX = record.getDouble(posXSetpointIdx);
				prevY = record.getDouble(posYSetpointIdx);
				nextX = prevX;
				nextY = prevY;
				endX = prevX;
				endY = prevY;
				startX = prevX;
				startY = prevY;
			}
			else {
				prevX = nextX;
				prevY = nextY;
				nextX = record.getDouble(posXSetpointIdx);
				nextY = record.getDouble(posYSetpointIdx);
		
				if(nextX != prevX) {
					if(nextX == startX) {
						pointX = 0;
					}
					else {
						pointX++;
						if(pointX == sizeX) {
							endX = nextX;
							sizeX++;
							if(sizeY > 1) {
								firstY = false;
							}
						}
						
						if(stepX == 0.0) {
							stepX = nextX - prevX;
						}
						else {
							double delta = nextX - prevX;
							if(equal(stepX, delta, MAXIMUM_STEP_ERROR)) {
								stepX = (stepX + delta) / 2.0;
							}
							else {
								throw new ConverterException("Data point X-position step error: " + delta + ": at index: " + dataRecordIndex);
							}
						}
					}
				}
		
				if (nextY != prevY) {
					if(nextY == startY) {
						pointY = 0;
					}
					else {
						pointY++;
						if(pointY == sizeY) {
							endY = nextY;
							sizeY++;
							if(sizeX > 1) {
								firstX = false;
							}
						}
						
						if(stepY == 0.0) {
							stepY = nextY - prevY;
						}
						else {
							double delta = nextY - prevY;
							if(equal(stepY, delta, MAXIMUM_STEP_ERROR)) {
								stepY = (stepY + delta) / 2.0;
							}
							else {
								throw new ConverterException("Data point Y-position step error: " + delta + ": at index: " + dataRecordIndex);
							}
						}
					}
				}
			}
		}
		catch(RecordFormatException e) {
			throw new ConverterException("Record format exception getting setpoint " + 
											"from data record with index: " + dataRecordIndex, e);
		}
		
		if(firstX) {
			a = getCdfAttribute(MapXY11.StepX());
			a.getEntry(0).putData(CDF.CDF_DOUBLE, stepX);
			
			a = getCdfAttribute(MapXY11.SizeX());
			a.getEntry(0).putData(CDF.CDF_UINT2, sizeX);
			
			a = getCdfAttribute(MapXY11.StartX());
			a.getEntry(0).putData(CDF.CDF_DOUBLE, startX);
			
			a = getCdfAttribute(MapXY11.EndX());
			a.getEntry(0).putData(CDF.CDF_DOUBLE, endX);
		}
		
		if(firstY) {
			a = getCdfAttribute(MapXY11.StepY());
			a.getEntry(0).putData(CDF.CDF_DOUBLE, stepY);
			
			a = getCdfAttribute(MapXY11.SizeY());
			a.getEntry(0).putData(CDF.CDF_UINT2, sizeY);
			
			a = getCdfAttribute(MapXY11.StartY());
			a.getEntry(0).putData(CDF.CDF_DOUBLE, startY);
			
			a = getCdfAttribute(MapXY11.EndY());
			a.getEntry(0).putData(CDF.CDF_DOUBLE, endY);
		}
		
		cdfRecord = new CDFRecord(cdfEvent);
		
		// MapXY11 Category //
		
		try {
			cdfRecord.setValueByName(MapXY11.I(), pointX);
			cdfRecord.setValueByName(MapXY11.J(), pointY);
			
			try {
				posX = record.getDouble(posXFeedbackIdx);
			}
			catch (RecordFormatException e) {
				posX = nextX; // Fall-back to setpointX //
			}
			cdfRecord.setValueByName(MapXY11.X(), posX);
			
			
			try {
				posY = record.getDouble(posYFeedbackIdx);
			}
			catch (RecordFormatException e) {
				posY = nextY; // Fall-back to setpointY //
			}
			cdfRecord.setValueByName(MapXY11.Y(), posY);
		}
		catch(RecordFormatException e) {
			throw new ConverterException("Exception while setting MapXY parameters on CDF record.", e);
		}
		
		// BeamI11 Category //
		
		if(hasBeamCurrent()) {
			try {
				if(srCurrentIdx >= 0) {
					double srCurrent = record.getDouble(srCurrentIdx);
					cdfRecord.setValueByName(BeamI11.SR(), srCurrent);
				}
			
				double mcsCurrent;
				for(int i=0; i<mcsCurrentIdx.length; i++) {
					if(mcsCurrentIdx[i] >= 0) {
						mcsCurrent = record.getDouble(mcsCurrentIdx[i]);
						cdfRecord.setValueByName(BeamI11.I(i), mcsCurrent);
					}
				}		
			}
			catch(RecordFormatException e) {
				throw new ConverterException("");
			}
		}
		
		// MCA10 Category //
		
		if(hasSingleElementDetector()) {
			try {
				if(sedNChannels < 0) {
					sedNChannels = sedDefaultNChannels;
				}
				
				long sedFastCount = 0L;
				if(sedFastCountIdx >= 0) {
					sedFastCount = record.getLong(sedFastCountIdx);
					cdfRecord.setValueByName(MCA10.FastCount(0), sedFastCount);
				}
				
				long sedSlowCount = 0L;
				if(sedSlowCountIdx >= 0) {
					sedSlowCount = record.getLong(sedSlowCountIdx);
					cdfRecord.setValueByName(MCA10.SlowCount(0), sedSlowCount);
				}
				
				if(sedDeadTimePctIdx >= 0) {
					double sedDeadTimePct  = record.getDouble(sedDeadTimePctIdx);
					cdfRecord.setValueByName(MCA10.DeadTimePct(0), sedDeadTimePct);
				}
			
				if(sedElapsedRealTimeIdx >= 0) {
					double sedElapsedRealTime = record.getDouble(sedElapsedRealTimeIdx);
					cdfRecord.setValueByName(MCA10.ElapsedRealTime(0), sedElapsedRealTime);
				}
			
				if(sedElpasedLiveTimeIdx >= 0) {
					double sedElapsedLiveTime = record.getDouble(sedElpasedLiveTimeIdx);
					cdfRecord.setValueByName(MCA10.ElapsedLiveTime(0), sedElapsedLiveTime);
				}
				
				if(sedSpectrum == null) {
					sedSpectrum = new long[sedNChannels];
				}
				
				long sedSpectrumOffset = record.getLong(sedSpectrumIdx);
				DAFSpectrumRecord specRecord = dafSpectraParser.parseRecord(sedSpectrumOffset, sedNChannels);
				System.arraycopy(specRecord.getLongArray(), 0, sedSpectrum, 0, sedNChannels);
				cdfRecord.setValueByName(MCA10.Spectrum(0), sedSpectrum);
			}
			catch(IOException e) {
				throw new ConverterException("Input error while parsing record from spectra data file.", e);
			}
			catch(DataFormatException e) {
				throw new ConverterException("Data format error while parsing record from spectra file.", e);
			}
			catch(RecordFormatException e) {
				throw new ConverterException("Record exception while getting data from record.", e);
			}
		}
		else if(hasFourElementDetector()) {
			try {
				if(fedNChannels < 0) {
					fedNChannels = fedDefaultNChannels;
				}
				 
				if(allGreaterThanOrEqual(fedFastCountIdx, 0)) {
					long fedFastCount;
					for(int i=0; i<fedFastCountIdx.length; i++) {
						fedFastCount = record.getLong(fedFastCountIdx[i]);
						cdfRecord.setValueByName(MCA10.FastCount(i), fedFastCount);
					}
				}
				
				if(allGreaterThanOrEqual(fedSlowCountIdx, 0)) {
					long fedSlowCount;
					for(int i=0; i<fedSlowCountIdx.length; i++) {
						fedSlowCount = record.getLong(fedSlowCountIdx[i]);
						cdfRecord.setValueByName(MCA10.SlowCount(i), fedSlowCount);
					}
				}
			
				if(allGreaterThanOrEqual(fedDeadTimePctIdx, 0)) {
					double fedDeadTimePct;
					for(int i=0; i<fedDeadTimePctIdx.length; i++) {
						fedDeadTimePct = record.getDouble(fedDeadTimePctIdx[i]);
						cdfRecord.setValueByName(MCA10.DeadTimePct(i), fedDeadTimePct);
					}
				}
				
				if(allGreaterThanOrEqual(fedElapsedRealTimeIdx, 0)) {
					double fedElapsedRealTime;
					for(int i=0; i<fedElapsedRealTimeIdx.length; i++) {
						fedElapsedRealTime = record.getDouble(fedElapsedRealTimeIdx[i]);
						cdfRecord.setValueByName(MCA10.ElapsedRealTime(i), fedElapsedRealTime);
					}
				}
			
				if(allGreaterThanOrEqual(fedElpasedLiveTimeIdx, 0)) {
					double fedElapsedLiveTime;
					for(int i=0; i<fedElpasedLiveTimeIdx.length; i++) {
						fedElapsedLiveTime = record.getDouble(fedElpasedLiveTimeIdx[i]);
						cdfRecord.setValueByName(MCA10.ElapsedLiveTime(i), fedElapsedLiveTime);
					}
				}
			
				long fedSpecOffset;
				DAFSpectrumRecord fedSpecRecord;
			
				if(fedSumSpectrumIdx >= 0) {
					if(fedSumSpectrum == null) {
						fedSumSpectrum = new long[fedNChannels];
					}
					fedSpecOffset = record.getLong(fedSumSpectrumIdx);
					fedSpecRecord = dafSpectraParser.parseRecord(fedSpecOffset, fedNChannels);
					System.arraycopy(fedSpecRecord.getLongArray(), 0, fedSumSpectrum, 0, fedNChannels);
					cdfRecord.setValueByName(MCA10.SumSpectrum(), fedSumSpectrum);
				}
				
				if(allGreaterThanOrEqual(fedSpectrumIdx, 0)) {
					if(fedSpectrum == null) {
						fedSpectrum = new long[4][fedNChannels];
					}
					for(int i=0; i<fedSpectrumIdx.length; i++) {
						fedSpecOffset = record.getLong(fedSpectrumIdx[i]);
						fedSpecRecord = dafSpectraParser.parseRecord(fedSpecOffset, fedNChannels);
						System.arraycopy(fedSpecRecord.getLongArray(), 0, fedSpectrum[i], 0, fedNChannels);
						cdfRecord.setValueByName(MCA10.Spectrum(i), fedSpectrum[i]);
					}
				}
			}
			catch(IOException e) {
				throw new ConverterException("Input error while parsing record from spectra data file.", e);
			}
			catch(DataFormatException e) {
				throw new ConverterException("Data format error while parsing record from spectra file.", e);
			}
			catch(RecordFormatException e) {
				throw new ConverterException("Record exception while getting data from record.", e);
			}
		}
	
		try {
			cdf.putRecord(dataRecordIndex, cdfRecord.getEvent().getVariableIDs(), cdfRecord.getCDFValues());
		}
		catch(RecordFormatException e) {
			throw new ConverterException("Exception while adding data record to CDF file.", e);
		}
	}
	
	protected void processBkgdRecord(DAFRecord record) throws CDFException {

		if(bkgdRecordIndex == 0) {
			
			List<DAFElement> bkgdEventElements = record.getEvent().getElements();
			
			if(hasSingleElementDetector()) {
				
				if((sedNChannels < 0) && (sedNChannelsIdx >= 0)) {
					try {
						sedNChannels = record.getInt(sedNChannelsIdx);
					}
					catch (RecordFormatException e) {
						sedNChannels = sedDefaultNChannels;
					}
				}
				
				if(sedMaxEnergyIdx >= 0) {
					try {
						double sedMaxEnergy = record.getDouble(sedMaxEnergyIdx);
						sedMaxEnergy *= 1000; // Convert keV to eV //
						Attribute a = createCdfAttribute(MCA10.MaxEnergy(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedMaxEnergy);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.EVOLT);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(sedMaxEnergyIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(sedMaxEnergyIdx).getDescription());
					}
					catch (RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedEnergyGapTimeIdx >= 0) {
					try {
						double sedEnergyGapTime = record.getDouble(sedEnergyGapTimeIdx);
						Attribute a = createCdfAttribute(MCA10.EnergyGapTime(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedEnergyGapTime);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MICRO_SECOND);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(sedEnergyGapTimeIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(sedEnergyGapTimeIdx).getDescription());
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedEnergyPeakingTimeIdx >= 0) {
					try {
						double sedEnergyPeakingTime = record.getDouble(sedEnergyPeakingTimeIdx);
						Attribute a = createCdfAttribute(MCA10.EnergyPeakingTime(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedEnergyPeakingTime);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MICRO_SECOND);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(sedEnergyPeakingTimeIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(sedEnergyPeakingTimeIdx).getDescription());
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedPresetRealTimeIdx >= 0) {
					try {
						double sedPresetRealTime = record.getDouble(sedPresetRealTimeIdx);
						Attribute a = createCdfAttribute(MCA10.PresetRealTime(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, sedPresetRealTime);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.SECOND);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(sedPresetRealTimeIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(sedPresetRealTimeIdx).getDescription());
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
			}
			else if(hasFourElementDetector()) {
				
				if((fedNChannels < 0) && (fedNChannelsIdx >= 0)) {
					try {
						fedNChannels = record.getInt(fedNChannelsIdx);
					}
					catch (RecordFormatException e) {
						fedNChannels = fedDefaultNChannels;
					}
				}
							
				if(fedMaxEnergyIdx >= 0) {
					try {
						double fedMaxEnergy = record.getDouble(fedMaxEnergyIdx);
						fedMaxEnergy *= 1000; // Convert keV to eV //
						Attribute a = createCdfAttribute(MCA10.MaxEnergy(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedMaxEnergy);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.EVOLT);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(fedMaxEnergyIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(fedMaxEnergyIdx).getDescription());
					}
					catch (RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedEnergyGapTimeIdx >= 0) {
					try {
						double fedEnergyGapTime = record.getDouble(fedEnergyGapTimeIdx);
						Attribute a = createCdfAttribute(MCA10.EnergyGapTime(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedEnergyGapTime);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MICRO_SECOND);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(fedEnergyGapTimeIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(fedEnergyGapTimeIdx).getDescription());
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedEnergyPeakingTimeIdx >= 0) {
					try {
						double fedEnergyPeakingTime = record.getDouble(fedEnergyPeakingTimeIdx);
						Attribute a = createCdfAttribute(MCA10.EnergyPeakingTime(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedEnergyPeakingTime);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.MICRO_SECOND);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(fedEnergyPeakingTimeIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(fedEnergyPeakingTimeIdx).getDescription());
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedPresetRealTimeIdx >= 0) {
					try {
						double fedPresetRealTime = record.getDouble(fedPresetRealTimeIdx);
						Attribute a = createCdfAttribute(MCA10.PresetRealTime(), CDF.GLOBAL_SCOPE);
						createCdfEntry(a, 0, CDF.CDF_DOUBLE, fedPresetRealTime);
						createCdfEntry(a, 1, CDF.CDF_CHAR, StdUnits.SECOND);
						createCdfEntry(a, 2, CDF.CDF_CHAR, bkgdEventElements.get(fedPresetRealTimeIdx).getName());
						createCdfEntry(a, 3, CDF.CDF_CHAR, bkgdEventElements.get(fedPresetRealTimeIdx).getDescription());
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
			}
		}
	}

	protected void padMissingPoints() throws CDFException, ConverterException {
			
		int padX = sizeX - pointX;
		if(padX > 1) {
			try {
				for (int i = 1; i < padX; i++) {
					cdfRecord.setValueByName(MapXY11.I(), pointX + i);
					cdfRecord.setValueByName(MapXY11.X(), posX + (i * stepX));
					cdf.putRecord(dataRecordIndex + i, cdfRecord.getEvent().getVariableIDs(), cdfRecord.getCDFValues());
				}
			}
			catch(RecordFormatException e) {
				throw new ConverterException("Exception while padding data in the X-direction.");
			}
		}

		int padY = sizeY - pointY;
		if(padY > 1) {
			try {
				for (int i = 1; i < padY; i++) {
					cdfRecord.setValueByName(MapXY11.J(), pointY + i);
					cdfRecord.setValueByName(MapXY11.Y(), posY + (i * stepY));
					cdf.putRecord(dataRecordIndex + i, cdfRecord.getEvent().getVariableIDs(), cdfRecord.getCDFValues());
				}
			}
			catch(RecordFormatException e) {
				throw new ConverterException("Exception while padding data in the Y-direction.");
			}
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

	public void setDafDataFile(File dafDataFile) {
		this.dafDataFile = dafDataFile;
	}

	public void setDafSpectraFile(File dafSpectraFile) {
		this.dafSpectraFile = dafSpectraFile;
	}

	public void setDataEventId(int dataEventId) {
		this.dataEventId = dataEventId;
	}

	public void setBkgdEventId(int bkgdEventId) {
		this.bkgdEventId = bkgdEventId;
	}

	public void setPosXSetpointIdx(int posXSetpointIdx) {
		this.posXSetpointIdx = posXSetpointIdx;
	}

	public void setPosYSetpointIdx(int posYSetpointIdx) {
		this.posYSetpointIdx = posYSetpointIdx;
	}

	public void setPosXFeedbackIdx(int posXFeedbackIdx) {
		this.posXFeedbackIdx = posXFeedbackIdx;
	}

	public void setPosYFeedbackIdx(int posYFeedbackIdx) {
		this.posYFeedbackIdx = posYFeedbackIdx;
	}

	public void setSrCurrentIdx(int srCurrentIdx) {
		this.srCurrentIdx = srCurrentIdx;
	}

	public void setMcsCurrentIdx(int[] mcsCurrentIdx) {
		this.mcsCurrentIdx = mcsCurrentIdx;
	}

	public void setSedNChannelsIdx(int sedNChannelsIdx) {
		this.sedNChannelsIdx = sedNChannelsIdx;
	}

	public void setSedMaxEnergyIdx(int sedMaxEnergyIdx) {
		this.sedMaxEnergyIdx = sedMaxEnergyIdx;
	}

	public void setSedEnergyGapTimeIdx(int sedEnergyGapTimeIdx) {
		this.sedEnergyGapTimeIdx = sedEnergyGapTimeIdx;
	}

	public void setSedPresetRealTimeIdx(int sedPresetRealTimeIdx) {
		this.sedPresetRealTimeIdx = sedPresetRealTimeIdx;
	}

	public void setSedEnergyPeakingTimeIdx(int sedEnergyPeakingTimeIdx) {
		this.sedEnergyPeakingTimeIdx = sedEnergyPeakingTimeIdx;
	}

	public void setSedSpectrumIdx(int sedSpectrumIdx) {
		this.sedSpectrumIdx = sedSpectrumIdx;
	}

	public void setSedFastCountIdx(int sedFastCountIdx) {
		this.sedFastCountIdx = sedFastCountIdx;
	}

	public void setSedSlowCountIdx(int sedSlowCountIdx) {
		this.sedSlowCountIdx = sedSlowCountIdx;
	}

	public void setSedDeadTimePctIdx(int sedDeadTimePctIdx) {
		this.sedDeadTimePctIdx = sedDeadTimePctIdx;
	}

	public void setSedElapsedRealTimeIdx(int sedElapsedRealTimeIdx) {
		this.sedElapsedRealTimeIdx = sedElapsedRealTimeIdx;
	}

	public void setSedElpasedLiveTimeIdx(int sedElpasedLiveTimeIdx) {
		this.sedElpasedLiveTimeIdx = sedElpasedLiveTimeIdx;
	}

	public void setSedDefaultNChannels(int sedDefaultNChannels) {
		this.sedDefaultNChannels = sedDefaultNChannels;
	}

	public void setFedNChannelsIdx(int fedNChannelsIdx) {
		this.fedNChannelsIdx = fedNChannelsIdx;
	}

	public void setFedMaxEnergyIdx(int fedMaxEnergyIdx) {
		this.fedMaxEnergyIdx = fedMaxEnergyIdx;
	}

	public void setFedEnergyGapTimeIdx(int fedEnergyGapTimeIdx) {
		this.fedEnergyGapTimeIdx = fedEnergyGapTimeIdx;
	}

	public void setFedPresetRealTimeIdx(int fedPresetRealTimeIdx) {
		this.fedPresetRealTimeIdx = fedPresetRealTimeIdx;
	}

	public void setFedEnergyPeakingTimeIdx(int fedEnergyPeakingTimeIdx) {
		this.fedEnergyPeakingTimeIdx = fedEnergyPeakingTimeIdx;
	}

	public void setFedSumSpectrumIdx(int fedSumSpectrumIdx) {
		this.fedSumSpectrumIdx = fedSumSpectrumIdx;
	}

	public void setFedSpectrumIdx(int[] fedSpectrumIdx) {
		this.fedSpectrumIdx = fedSpectrumIdx;
	}

	public void setFedFastCountIdx(int[] fedFastCountIdx) {
		this.fedFastCountIdx = fedFastCountIdx;
	}

	public void setFedSlowCountIdx(int[] fedSlowCountIdx) {
		this.fedSlowCountIdx = fedSlowCountIdx;
	}

	public void setFedDeadTimePctIdx(int[] fedDeadTimePctIdx) {
		this.fedDeadTimePctIdx = fedDeadTimePctIdx;
	}

	public void setFedElapsedRealTimeIdx(int[] fedElapsedRealTimeIdx) {
		this.fedElapsedRealTimeIdx = fedElapsedRealTimeIdx;
	}

	public void setFedElpasedLiveTimeIdx(int[] fedElpasedLiveTimeIdx) {
		this.fedElpasedLiveTimeIdx = fedElpasedLiveTimeIdx;
	}
	
	public void setFedDefaultNChannels(int fedDefaultNChannels) {
		this.fedDefaultNChannels = fedDefaultNChannels;
	}

	public void setCustomRecordParsers(Collection<DAFRecordParser> customRecordParsers) {
		this.customRecordParsers = customRecordParsers;
	}

	public void setCdfSpectrumCompressionLevel(long cdfSpectrumCompressionLevel) {
		this.cdfSpectrumCompressionLevel[0] = Math.min(Math.max(cdfSpectrumCompressionLevel, 
												CDF_MIN_COMPRESSION_LEVEL), CDF_MAX_COMPRESSION_LEVEL);
	}
}
