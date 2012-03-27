package ca.sciencestudio.vespers.data.converter.factory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.coyote.http11.filters.BufferedInputFilter;

import com.ibatis.common.io.ReaderInputStream;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.daf.DAFDataParser;
import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.data.util.ElementOptions;
import ca.sciencestudio.util.Parameters;
import ca.sciencestudio.vespers.data.converter.AbstractMapXYVespersConverter;
import ca.sciencestudio.vespers.data.converter.MapXYVespersToCDFConverter;
import ca.sciencestudio.vespers.data.converter.factory.AbstractMapXYVespersConverterFactory.MapXYDAFEventsHelper;

public class MapXYVespersFromAMFConverterFactory extends AbstractMapXYVespersConverterFactory {

	private static final Pattern AM_DATA_FILE_BLANKLINE_PATTERN = Pattern.compile("\\s*");
	
	private static final Pattern AM_DATA_FILE_HEADER_SCAN_PATTERN = Pattern.compile("\\s*#\\s*Scan:\\s*(.*?)\\s*");
	private static final Pattern AM_DATA_FILE_HEADER_DATE_PATTERN = Pattern.compile("\\s*#\\s*Date:\\s*(.*?)\\s*");
	private static final Pattern AM_DATA_FILE_HEADER_SAMPLE_PATTERN = Pattern.compile("\\s*#\\s*Sample:\\s*(.*?)\\s*");
	private static final Pattern AM_DATA_FILE_HEADER_FACILITY_PATTERN = Pattern.compile("\\s*#\\s*Facility:\\s*(.*?\\s*)");
	
	private static final Pattern AM_DATA_FILE_HEADER_INAUGHT_PATTERN = Pattern.compile("\\s*#\\s*I0:\\s*(.*?)\\s*");
	private static final Pattern AM_DATA_FILE_HEADER_DETECTOR_PATTERN = Pattern.compile("\\s*#\\s*fff:\\s+(.*?)\\s*");
	
	private static final Pattern AM_DATA_FILE_HEADER_DASHLINE_PATTERN = Pattern.compile("\\s*#\\s*[\\-]+");
	private static final Pattern AM_DATA_FILE_HEADER_ELEMENTS_PATTERN = Pattern.compile("\\s*#\\s*Scan:\\s+(.*)");
	
	private static final Pattern AM_DATA_FILE_HEADER_DATA_PATTERN = Pattern.compile("\\s*([^#].*)");
	
	// Parameters for the sample position. //
	private ElementOptions posXSetpointOptions;
	private ElementOptions posYSetpointOptions;
	
	private ElementOptions posXFeedbackOptions;
	private ElementOptions posYFeedbackOptions;
	
	// Beam current (election or x-ray). //
	private ElementOptions srCurrentOptions;
	private List<ElementOptions> mcsCurrentOptions;
	
	// Parameters for the Single Element Detector. //
//	private DAFEventElementOptions sedNChannelsOptions;
//	private DAFEventElementOptions sedMaxEnergyOptions;
//	private DAFEventElementOptions sedEnergyGapTimeOptions;
//	private DAFEventElementOptions sedPresetRealTimeOptions;
//	private DAFEventElementOptions sedEnergyPeakingTimeOptions;
//	
//	private DAFEventElementOptions sedSpectrumOptions;
	private ElementOptions sedFastCountOptions;
	private ElementOptions sedSlowCountOptions;
	private ElementOptions sedDeadTimePctOptions;
	private ElementOptions sedElapsedRealTimeOptions;
	private ElementOptions sedElpasedLiveTimeOptions;
	
	private int sedDefaultNChannels = 2048; //AbstractMapXYVespersConverter.DEFAULT_SED_NCHANNELS;
	
	// Parameters for the Four Element Detector. //
//	private DAFEventElementOptions fedNChannelsOptions;
//	private DAFEventElementOptions fedMaxEnergyOptions;
//	private DAFEventElementOptions fedEnergyGapTimeOptions;
//	private DAFEventElementOptions fedPresetRealTimeOptions;
//	private DAFEventElementOptions fedEnergyPeakingTimeOptions;
//	
//	private DAFEventElementOptions fedSumSpectrumOptions;
//	private List<DAFEventElementOptions> fedSpectrumOptions;
	private List<ElementOptions> fedFastCountOptions;
	private List<ElementOptions> fedSlowCountOptions;
	private List<ElementOptions> fedDeadTimePctOptions;
	private List<ElementOptions> fedElapsedRealTimeOptions;
	private List<ElementOptions> fedElpasedLiveTimeOptions;
	
	private int fedDefaultNChannels = 2048; //AbstractMapXYVespersConverter.DEFAULT_FED_NCHANNELS;
	

	@Override
	public Converter getConverter(ConverterMap request) throws ConverterFactoryException {
		
		ConverterMap validRequest = validateRequest(request);
		boolean forceUpdate = validRequest.isForceUpdate();
		String fromFormat = validRequest.getFromFormat();
		String toFormat = validRequest.getToFormat();
		
		//MapXYVespersToCDFConverter converter = new MapXYVespersToCDFConverter(fromFormat, toFormat, forceUpdate);
		//prepareConverter(converter, validRequest);
		//return converter;
	
	
	//protected void prepareConverter(AbstractMapXYVespersConverter converter, ConverterMap request) throws ConverterFactoryException {
		
		File amDataFile = (File)request.get(REQUEST_KEY_DAF_DATA_FILE);
		if(!amDataFile.exists()) {
			throw new ConverterFactoryException("The required AM data file not found here: " + amDataFile);
		}
		
		File amSpectraFile = (File)request.get(REQUEST_KEY_DAF_SPEC_FILE);
		if(!amSpectraFile.exists()) {
			throw new ConverterFactoryException("The required AM spectra file not found here: " + amSpectraFile);
		}
		
//		DAFDataParser dafDataParser;
//		try {
//			dafDataParser = new DAFDataParser(dafDataFile, customRecordParsers);
//		}
//		catch(Exception e) {
//			throw new ConverterFactoryException("Exception while creating data parser for file: " + dafDataFile, e);
//		}
		
		//MapXYDAFEventsHelper eventsHelper = new MapXYDAFEventsHelper(dafDataParser);
		
		// Read Data File Header //
		
		BufferedReader amDataFileReader;
		try {
			amDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(amDataFile)));
		}
		catch(FileNotFoundException e) {
			throw new ConverterFactoryException("The required AM data file could not be openned: " + e.getMessage());
		}
		
		Matcher m;
		boolean scanNext = true;
		boolean elementsNext = false;
		String line = amDataFileReader.readLine();
		
		while(line != null) {
			
			if(AM_DATA_FILE_BLANKLINE_PATTERN.matcher(line).matches()) {
				// ignore lines containing only whitespace //
				continue;
			}
			
			if(scanNext) {
				m = AM_DATA_FILE_HEADER_SCAN_PATTERN.matcher(line);
				if(m.matches()) {
					//setScanName
					scanNext = false;
					continue;
				}
				
				//throw! 
			}
				
			
			if(elementsNext) {
				m = AM_DATA_FILE_HEADER_ELEMENTS_PATTERN.matcher(line);
				if(m.matches()) {
					
					elementsNext = false;
					continue;
				}
				
				//throw!
			}
			
			m = AM_DATA_FILE_HEADER_DATE_PATTERN.matcher(line);
			if(m.matches()) {
			
				continue;
			}
			
			
			m = AM_DATA_FILE_HEADER_SAMPLE_PATTERN.matcher(line);
			if(m.matches()) {
				
				continue;
			}
			
			m = AM_DATA_FILE_HEADER_FACILITY_PATTERN.matcher(line);
			if(m.matches()) {
				
				continue;
			}
			
			
			m = AM_DATA_FILE_HEADER_DETECTOR_PATTERN.matcher(line);
			if(m.matches()) {
				
				continue;
			}
			
			m = AM_DATA_FILE_HEADER_INAUGHT_PATTERN.matcher(line);
			if(m.matches()) {
				
				continue;
			}
			
			m = AM_DATA_FILE_HEADER_DASHLINE_PATTERN.matcher(line);
			if(m.matches()) {
				
				elementsNext = true;
				continue;
			}
		
		}
		
		
	
		
		
		
		
		int posXSetpointIdx = -1;
		int posYSetpointIdx = -1;
		for(DAFEvent event : eventsHelper.getEvents()) {
			posXSetpointIdx = posXSetpointOptions.getElementIndex(event);
			if(posXSetpointIdx >= 0) {
				posYSetpointIdx = posYSetpointOptions.getElementIndex(event);
				if(posYSetpointIdx >= 0) {
					eventsHelper.setDataEventId(event.getId());
					break;
				}
			}
		}
		
		DAFEvent dataEvent = eventsHelper.getDataEvent();
		if(dataEvent == null) {
			throw new ConverterFactoryException("No event found that contains the MapXY position coordinates.");
		}
		
		converter.setCustomRecordParsers(customRecordParsers);
		converter.setScanName((String)request.get(REQUEST_KEY_SAMPLE_NAME));
		converter.setScanEndDate((Date)request.get(REQUEST_KEY_SCAN_END_DATE));
		converter.setScanStartDate((Date)request.get(REQUEST_KEY_SCAN_START_DATE));
		converter.setScanParams((Parameters)request.get(REQUEST_KEY_SCAN_PARAMS));
		converter.setSampleName((String)request.get(REQUEST_KEY_SAMPLE_NAME));
		converter.setProjectName((String)request.get(REQUEST_KEY_PROJECT_NAME));
		converter.setSessionName((String)request.get(REQUEST_KEY_SESSION_NAME));
		converter.setExperimentName((String)request.get(REQUEST_KEY_EXPERIMENT_NAME));
		converter.setTechniqueName((String)request.get(REQUEST_KEY_TECHNIQUE_NAME));
		converter.setInstrumentName((String)request.get(REQUEST_KEY_INSTRUMENT_NAME));
		converter.setFacilityName((String)request.get(REQUEST_KEY_FACILITY_NAME));
		converter.setLaboratoryName((String)request.get(REQUEST_KEY_LABORATORY_NAME));
		
		converter.setDafDataFile(dafDataFile);
		converter.setDafSpectraFile(dafSpectraFile);
		converter.setDataEventId(eventsHelper.getDataEventId());
		
		converter.setPosXSetpointIdx(posXSetpointIdx);
		converter.setPosYSetpointIdx(posYSetpointIdx);
		converter.setPosXFeedbackIdx(posXFeedbackOptions.getElementIndex(dataEvent));
		converter.setPosYFeedbackIdx(posYFeedbackOptions.getElementIndex(dataEvent));
		
		prepareBeamCurrent(converter, eventsHelper);
		
		if(prepareSingleElementDetector(converter, eventsHelper)) {
			return;
		}
		
		if(prepareFourElementDetector(converter, eventsHelper)) {
			return;
		}
			
		throw new ConverterFactoryException("No MultiChannel Analyzer spectrum found in DAF data file.");
	}
	
	protected void prepareBeamCurrent(AbstractMapXYVespersConverter converter, MapXYDAFEventsHelper eventsHelper) {
		
		DAFEvent dataEvent = eventsHelper.getDataEvent();
		int[] mcsCurrentIdx = new int[mcsCurrentOptions.size()];
		for(int idx=0; idx<mcsCurrentIdx.length; idx++) {
			mcsCurrentIdx[idx] = mcsCurrentOptions.get(idx).getElementIndex(dataEvent);
		}
		int srCurrentIdx = srCurrentOptions.getElementIndex(dataEvent);
		
		converter.setMcsCurrentIdx(mcsCurrentIdx);
		converter.setSrCurrentIdx(srCurrentIdx);
	}
	
	protected boolean prepareSingleElementDetector(AbstractMapXYVespersConverter converter, MapXYDAFEventsHelper eventsHelper) {
		
		DAFEvent dataEvent = eventsHelper.getDataEvent();
		DAFEvent bkgdEvent = eventsHelper.getBkgdEvent();
		int sedSpectrumIdx = sedSpectrumOptions.getElementIndex(dataEvent);
		int sedFastCountIdx = sedFastCountOptions.getElementIndex(dataEvent);
		int sedSlowCountIdx = sedSlowCountOptions.getElementIndex(dataEvent);
		int sedDeadTimePctIdx = sedDeadTimePctOptions.getElementIndex(dataEvent);
		int sedElapsedRealTimeIdx = sedElapsedRealTimeOptions.getElementIndex(dataEvent);
		int sedElpasedLiveTimeIdx = sedElpasedLiveTimeOptions.getElementIndex(dataEvent);
		
		if(sedSpectrumIdx < 0) {
			return false;
		}
			
		int sedNChannelsIdx = -1;
		int sedMaxEnergyIdx = -1;
		int sedEnergyGapTimeIdx = -1;
		int sedPresetRealTimeIdx = -1;
		int sedEnergyPeakingTimeIdx = -1;		
		if(bkgdEvent == null) {
			for(DAFEvent event : eventsHelper.getEvents()) {
				sedNChannelsIdx = sedNChannelsOptions.getElementIndex(event);
				sedMaxEnergyIdx = sedMaxEnergyOptions.getElementIndex(event);
				sedEnergyGapTimeIdx = sedEnergyGapTimeOptions.getElementIndex(event);
				sedPresetRealTimeIdx = sedPresetRealTimeOptions.getElementIndex(event);
				sedEnergyPeakingTimeIdx = sedEnergyPeakingTimeOptions.getElementIndex(event);
				if((sedMaxEnergyIdx >= 0) || (sedNChannelsIdx >= 0) || (sedEnergyGapTimeIdx >= 0) ||
						(sedPresetRealTimeIdx >= 0) || (sedEnergyPeakingTimeIdx >= 0)) {
					eventsHelper.setBkgdEventId(event.getId());
					bkgdEvent = event;
					break;
				}
			}
		}
		else {
			sedNChannelsIdx = sedNChannelsOptions.getElementIndex(bkgdEvent);
			sedMaxEnergyIdx = sedMaxEnergyOptions.getElementIndex(bkgdEvent);
			sedEnergyGapTimeIdx = sedEnergyGapTimeOptions.getElementIndex(bkgdEvent);
			sedPresetRealTimeIdx = sedPresetRealTimeOptions.getElementIndex(bkgdEvent);
			sedEnergyPeakingTimeIdx = sedEnergyPeakingTimeOptions.getElementIndex(bkgdEvent);
		}
		
		if(bkgdEvent != null) {
			converter.setBkgdEventId(bkgdEvent.getId());
		}
		
		converter.setSedMaxEnergyIdx(sedMaxEnergyIdx);
		converter.setSedNChannelsIdx(sedNChannelsIdx);
		converter.setSedEnergyGapTimeIdx(sedEnergyGapTimeIdx);
		converter.setSedPresetRealTimeIdx(sedPresetRealTimeIdx);
		converter.setSedEnergyPeakingTimeIdx(sedEnergyPeakingTimeIdx);
		
		converter.setSedSpectrumIdx(sedSpectrumIdx);
		converter.setSedFastCountIdx(sedFastCountIdx);
		converter.setSedSlowCountIdx(sedSlowCountIdx);
		converter.setSedDeadTimePctIdx(sedDeadTimePctIdx);
		converter.setSedElapsedRealTimeIdx(sedElapsedRealTimeIdx);
		converter.setSedElpasedLiveTimeIdx(sedElpasedLiveTimeIdx);
		
		//converter.setSedDefaultMaxEnergy(sedDefaultMaxEnergy);
		converter.setSedDefaultNChannels(sedDefaultNChannels);
		return true;
	}
	
	
	protected boolean prepareFourElementDetector(AbstractMapXYVespersConverter converter, MapXYDAFEventsHelper eventsHelper) {
		
		int nElements = 4;
		DAFEvent dataEvent = eventsHelper.getDataEvent();
		DAFEvent bkgdEvent = eventsHelper.getBkgdEvent();
		int[] fedSpectrumIdx = new int[nElements];
		int[] fedFastCountIdx = new int[nElements];			
		int[] fedSlowCountIdx = new int[nElements];
		int[] fedDeadTimePctIdx = new int[nElements];
		int[] fedElapsedRealTimeIdx = new int[nElements];
		int[] fedElpasedLiveTimeIdx = new int[nElements];
		for(int idx=0; idx<nElements; idx++) {
			fedSpectrumIdx[idx] = fedSpectrumOptions.get(idx).getElementIndex(dataEvent);
			fedFastCountIdx[idx] = fedFastCountOptions.get(idx).getElementIndex(dataEvent);
			fedSlowCountIdx[idx] = fedSlowCountOptions.get(idx).getElementIndex(dataEvent);
			fedDeadTimePctIdx[idx] = fedDeadTimePctOptions.get(idx).getElementIndex(dataEvent);
			fedElapsedRealTimeIdx[idx] = fedElapsedRealTimeOptions.get(idx).getElementIndex(dataEvent);
			fedElpasedLiveTimeIdx[idx] = fedElpasedLiveTimeOptions.get(idx).getElementIndex(dataEvent);
		}
		int fedSumSpectrumIdx = fedSumSpectrumOptions.getElementIndex(dataEvent);
		
		if((fedSumSpectrumIdx < 0) && oneLessThan(fedSpectrumIdx, 0)) {
			return false;
		}
			
		int fedNChannelsIdx = -1; 
		int fedMaxEnergyIdx = -1;
		int fedEnergyGapTimeIdx = -1;
		int fedPresetRealTimeIdx = -1;
		int fedEnergyPeakingTimeIdx = -1;
		if(bkgdEvent == null) {
			for(DAFEvent event : eventsHelper.getEvents()) {
				fedNChannelsIdx = fedNChannelsOptions.getElementIndex(event);
				fedMaxEnergyIdx = fedMaxEnergyOptions.getElementIndex(event);
				fedEnergyGapTimeIdx = fedEnergyGapTimeOptions.getElementIndex(event); 
				fedPresetRealTimeIdx = fedPresetRealTimeOptions.getElementIndex(event);
				fedEnergyPeakingTimeIdx = fedEnergyPeakingTimeOptions.getElementIndex(event);
				if((fedNChannelsIdx >= 0) || (fedMaxEnergyIdx >= 0) || (fedEnergyGapTimeIdx >= 0) ||
						(fedPresetRealTimeIdx >= 0) || (fedEnergyPeakingTimeIdx >= 0)) {
					eventsHelper.setBkgdEventId(event.getId());
					bkgdEvent = event;
					break;
				}
			}
		}
		else {
			fedNChannelsIdx = fedNChannelsOptions.getElementIndex(bkgdEvent);
			fedMaxEnergyIdx = fedMaxEnergyOptions.getElementIndex(bkgdEvent);
			fedEnergyGapTimeIdx = fedEnergyGapTimeOptions.getElementIndex(bkgdEvent); 
			fedPresetRealTimeIdx = fedPresetRealTimeOptions.getElementIndex(bkgdEvent);
			fedEnergyPeakingTimeIdx = fedEnergyPeakingTimeOptions.getElementIndex(bkgdEvent);
		}
		
		if(bkgdEvent != null) {
			converter.setBkgdEventId(bkgdEvent.getId());
		}
		
		converter.setFedNChannelsIdx(fedNChannelsIdx);
		converter.setFedMaxEnergyIdx(fedMaxEnergyIdx);
		converter.setFedEnergyGapTimeIdx(fedEnergyGapTimeIdx);
		converter.setFedPresetRealTimeIdx(fedPresetRealTimeIdx);
		converter.setFedEnergyPeakingTimeIdx(fedEnergyPeakingTimeIdx);
		
		converter.setFedSpectrumIdx(fedSpectrumIdx);
		converter.setFedFastCountIdx(fedFastCountIdx);
		converter.setFedSlowCountIdx(fedSlowCountIdx);
		converter.setFedDeadTimePctIdx(fedDeadTimePctIdx);
		converter.setFedElapsedRealTimeIdx(fedElapsedRealTimeIdx);
		converter.setFedElpasedLiveTimeIdx(fedElpasedLiveTimeIdx);
		converter.setFedSumSpectrumIdx(fedSumSpectrumIdx);
		
		converter.setFedDefaultNChannels(fedDefaultNChannels);
		return true;
	}
		
	protected static class MapXYDAFEventsHelper {
		
		private int dataEventId = -1;
		private int bkgdEventId = -1;
		private DAFDataParser dafDataParser; 
		
		public MapXYDAFEventsHelper(DAFDataParser dafDataParser) {
			this.dafDataParser = dafDataParser;
		}

		public List<DAFEvent> getEvents() {
			return dafDataParser.getEvents();
		}
		
		public DAFEvent getDataEvent() {
			return dafDataParser.getEventById(dataEventId);
		}
		public int getDataEventId() {
			return dataEventId;
		}
		public void setDataEventId(int dataEventId) {
			this.dataEventId = dataEventId;
		}
		
		public DAFEvent getBkgdEvent() {
			return dafDataParser.getEventById(bkgdEventId);
		}
		public int getBkgdEventId() {
			return bkgdEventId;
		}
		public void setBkgdEventId(int bkgdEventId) {
			this.bkgdEventId = bkgdEventId;
		}
	}
}
