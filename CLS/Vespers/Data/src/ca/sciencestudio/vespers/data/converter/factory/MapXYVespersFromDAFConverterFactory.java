/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersFromDAFConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Collection;

import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.daf.DAFDataParser;
import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.daf.DAFEventElementOptions;
import ca.sciencestudio.data.daf.DAFRecordParser;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.vespers.data.converter.MapXYVespersFromDAFConverter;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class MapXYVespersFromDAFConverterFactory extends AbstractMapXYVespersConverterFactory implements StdScanParams {

	private static final String[] SUPPORTED_FROM_FORMATS = { ConverterMap.DEFAULT_FROM_FORMAT, "DAF" };
	
	// Parameters for the sample position. //
	private DAFEventElementOptions posXSetpointOptions;
	private DAFEventElementOptions posYSetpointOptions;
	
	private DAFEventElementOptions posXFeedbackOptions;
	private DAFEventElementOptions posYFeedbackOptions;
	
	// Beam current (election or x-ray). //
	private DAFEventElementOptions srCurrentOptions;
	private List<DAFEventElementOptions> mcsCurrentOptions;
	
	// Parameters for the Single Element Detector. //
	private DAFEventElementOptions sedNChannelsOptions;
	private DAFEventElementOptions sedMaxEnergyOptions;
	private DAFEventElementOptions sedEnergyGapTimeOptions;
	private DAFEventElementOptions sedPresetRealTimeOptions;
	private DAFEventElementOptions sedEnergyPeakingTimeOptions;
	
	private DAFEventElementOptions sedSpectrumOptions;
	private DAFEventElementOptions sedFastCountOptions;
	private DAFEventElementOptions sedSlowCountOptions;
	private DAFEventElementOptions sedDeadTimePctOptions;
	private DAFEventElementOptions sedElapsedRealTimeOptions;
	private DAFEventElementOptions sedElpasedLiveTimeOptions;
	
	private int sedDefaultNChannels = MapXYVespersFromDAFConverter.DEFAULT_SED_NCHANNELS;
	
	// Parameters for the Four Element Detector. //
	private DAFEventElementOptions fedNChannelsOptions;
	private DAFEventElementOptions fedMaxEnergyOptions;
	private DAFEventElementOptions fedEnergyGapTimeOptions;
	private DAFEventElementOptions fedPresetRealTimeOptions;
	private DAFEventElementOptions fedEnergyPeakingTimeOptions;
	
	private DAFEventElementOptions fedSumSpectrumOptions;
	private List<DAFEventElementOptions> fedSpectrumOptions;
	private List<DAFEventElementOptions> fedFastCountOptions;
	private List<DAFEventElementOptions> fedSlowCountOptions;
	private List<DAFEventElementOptions> fedDeadTimePctOptions;
	private List<DAFEventElementOptions> fedElapsedRealTimeOptions;
	private List<DAFEventElementOptions> fedElpasedLiveTimeOptions;
	
	private int fedDefaultNChannels = MapXYVespersFromDAFConverter.DEFAULT_FED_NCHANNELS;
	
	private Collection<DAFRecordParser> customRecordParsers;
	
	@Override
	public Converter getConverter(ConverterMap request) throws ConverterFactoryException {
		
		request = validateRequest(request);
		
		boolean forceUpdate = request.isForceUpdate();
		String fromFormat = request.getFromFormat();
		String toFormat = request.getToFormat();
		
		File dafDataFile = (File)request.get(REQUEST_KEY_DATA_FILE);
		if(!dafDataFile.exists()) {
			throw new ConverterFactoryException("The required DAF data file not found here: " + dafDataFile);
		}
		
		File dafSpectraFile = (File)request.get(REQUEST_KEY_SPECTRA_FILE);
		if(!dafSpectraFile.exists()) {
			throw new ConverterFactoryException("The required DAF spectra file not found here: " + dafSpectraFile);
		}
		
		DAFDataParser dafDataParser;
		try {
			dafDataParser = new DAFDataParser(dafDataFile, customRecordParsers);
		}
		catch(Exception e) {
			throw new ConverterFactoryException("Exception while creating data parser for file: " + dafDataFile, e);
		}
		
		MapXYDAFEventsHelper eventsHelper = new MapXYDAFEventsHelper(dafDataParser);
		
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
		
		MapXYVespersFromDAFConverter converter;
		try {
			converter = new MapXYVespersFromDAFConverter(fromFormat, toFormat, forceUpdate);
		}
		catch(ConverterException e) {
			throw new ConverterFactoryException(e);
		}
		
		converter.setCustomRecordParsers(customRecordParsers);
		converter.setScanName((String)request.get(REQUEST_KEY_SCAN_NAME));
		converter.setScanEndDate((Date)request.get(REQUEST_KEY_SCAN_END_DATE));
		converter.setScanStartDate((Date)request.get(REQUEST_KEY_SCAN_START_DATE));
		converter.setScanParams((Parameters)request.get(REQUEST_KEY_SCAN_PARAMS));
		converter.setSampleName((String)request.get(REQUEST_KEY_SAMPLE_NAME));
		converter.setProjectName((String)request.get(REQUEST_KEY_PROJECT_NAME));
		converter.setSessionName((String)request.get(REQUEST_KEY_SESSION_NAME));
		converter.setExperimentName((String)request.get(REQUEST_KEY_EXPERIMENT_NAME));
		converter.setTechniqueName((String)request.get(REQUEST_KEY_TECHNIQUE_NAME));
		converter.setInstrumentName((String)request.get(REQUEST_KEY_INSTRUMENT_NAME));
		converter.setFacilityName((String)request.get(REQUEST_KEY_FACILITY_LONG_NAME));
		converter.setLaboratoryName((String)request.get(REQUEST_KEY_LABORATORY_LONG_NAME));
		
		converter.setDafDataFile(dafDataFile);
		converter.setDafSpectraFile(dafSpectraFile);
		converter.setDataEventId(eventsHelper.getDataEventId());
		
		converter.setPosXSetpointIdx(posXSetpointIdx);
		converter.setPosYSetpointIdx(posYSetpointIdx);
		converter.setPosXFeedbackIdx(posXFeedbackOptions.getElementIndex(dataEvent));
		converter.setPosYFeedbackIdx(posYFeedbackOptions.getElementIndex(dataEvent));
		
		prepareBeamCurrent(converter, eventsHelper);
		
		if(prepareSingleElementDetector(converter, eventsHelper)) {
			prepareAdapter(converter, request);
			return converter;
		}
		
		if(prepareFourElementDetector(converter, eventsHelper)) {
			prepareAdapter(converter, request);
			return converter;
		}
		
		throw new ConverterFactoryException("MultiChannel Analyzer spectrum not found in DAF data file.");
	}
	
	protected void prepareBeamCurrent(MapXYVespersFromDAFConverter converter, MapXYDAFEventsHelper eventsHelper) {
		
		DAFEvent dataEvent = eventsHelper.getDataEvent();
		int[] mcsCurrentIdx = new int[mcsCurrentOptions.size()];
		for(int idx=0; idx<mcsCurrentIdx.length; idx++) {
			mcsCurrentIdx[idx] = mcsCurrentOptions.get(idx).getElementIndex(dataEvent);
		}
		int srCurrentIdx = srCurrentOptions.getElementIndex(dataEvent);
		
		converter.setMcsCurrentIdx(mcsCurrentIdx);
		converter.setSrCurrentIdx(srCurrentIdx);
	}
	
	protected boolean prepareSingleElementDetector(MapXYVespersFromDAFConverter converter, MapXYDAFEventsHelper eventsHelper) {
		
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
	
	protected boolean prepareFourElementDetector(MapXYVespersFromDAFConverter converter, MapXYDAFEventsHelper eventsHelper) {
		
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
	
	@Override
	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {
		
		if(!oneEqual(SUPPORTED_FROM_FORMATS, request.getFromFormat())) {
			throw new ConverterFactoryException("Convert FROM format, " + request.getFromFormat() + ", not supported.");
		}
		
		if(!adapterSupports(request)) {
			throw new ConverterFactoryException("Convert TO format, " + request.getToFormat() + ", not supported.");
		}
		
		// Need to call this first to get DAF data file. //
		request = super.validateRequest(request);
		
		return request;
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

	
	public void setPosXSetpointOptions(DAFEventElementOptions posXSetpointOptions) {
		this.posXSetpointOptions = posXSetpointOptions;
	}

	public void setPosYSetpointOptions(DAFEventElementOptions posYSetpointOptions) {
		this.posYSetpointOptions = posYSetpointOptions;
	}

	public void setPosXFeedbackOptions(DAFEventElementOptions posXFeedbackOptions) {
		this.posXFeedbackOptions = posXFeedbackOptions;
	}

	public void setPosYFeedbackOptions(DAFEventElementOptions posYFeedbackOptions) {
		this.posYFeedbackOptions = posYFeedbackOptions;
	}

	public void setSrCurrentOptions(DAFEventElementOptions srCurrentOptions) {
		this.srCurrentOptions = srCurrentOptions;
	}

	public void setMcsCurrentOptions(List<DAFEventElementOptions> mcsCurrentOptions) {
		this.mcsCurrentOptions = mcsCurrentOptions;
	}

	public void setSedNChannelsOptions(DAFEventElementOptions sedNChannelsOptions) {
		this.sedNChannelsOptions = sedNChannelsOptions;
	}

	public void setSedMaxEnergyOptions(DAFEventElementOptions sedMaxEnergyOptions) {
		this.sedMaxEnergyOptions = sedMaxEnergyOptions;
	}

	public void setSedEnergyGapTimeOptions(DAFEventElementOptions sedEnergyGapTimeOptions) {
		this.sedEnergyGapTimeOptions = sedEnergyGapTimeOptions;
	}

	public void setSedPresetRealTimeOptions(DAFEventElementOptions sedPresetRealTimeOptions) {
		this.sedPresetRealTimeOptions = sedPresetRealTimeOptions;
	}

	public void setSedEnergyPeakingTimeOptions(DAFEventElementOptions sedEnergyPeakingTimeOptions) {
		this.sedEnergyPeakingTimeOptions = sedEnergyPeakingTimeOptions;
	}

	public void setSedSpectrumOptions(DAFEventElementOptions sedSpectrumOptions) {
		this.sedSpectrumOptions = sedSpectrumOptions;
	}

	public void setSedFastCountOptions(DAFEventElementOptions sedFastCountOptions) {
		this.sedFastCountOptions = sedFastCountOptions;
	}

	public void setSedSlowCountOptions(DAFEventElementOptions sedSlowCountOptions) {
		this.sedSlowCountOptions = sedSlowCountOptions;
	}

	public void setSedDeadTimePctOptions(DAFEventElementOptions sedDeadTimePctOptions) {
		this.sedDeadTimePctOptions = sedDeadTimePctOptions;
	}

	public void setSedElapsedRealTimeOptions(DAFEventElementOptions sedElapsedRealTimeOptions) {
		this.sedElapsedRealTimeOptions = sedElapsedRealTimeOptions;
	}

	public void setSedElpasedLiveTimeOptions(DAFEventElementOptions sedElpasedLiveTimeOptions) {
		this.sedElpasedLiveTimeOptions = sedElpasedLiveTimeOptions;
	}

	public void setSedDefaultNChannels(int sedDefaultNChannels) {
		this.sedDefaultNChannels = sedDefaultNChannels;
	}

	public void setFedNChannelsOptions(DAFEventElementOptions fedNChannelsOptions) {
		this.fedNChannelsOptions = fedNChannelsOptions;
	}

	public void setFedMaxEnergyOptions(DAFEventElementOptions fedMaxEnergyOptions) {
		this.fedMaxEnergyOptions = fedMaxEnergyOptions;
	}

	public void setFedPresetRealTimeOptions(DAFEventElementOptions fedPresetRealTimeOptions) {
		this.fedPresetRealTimeOptions = fedPresetRealTimeOptions;
	}

	public void setFedEnergyGapTimeOptions(DAFEventElementOptions fedEnergyGapTimeOptions) {
		this.fedEnergyGapTimeOptions = fedEnergyGapTimeOptions;
	}

	public void setFedEnergyPeakingTimeOptions(DAFEventElementOptions fedEnergyPeakingTimeOptions) {
		this.fedEnergyPeakingTimeOptions = fedEnergyPeakingTimeOptions;
	}

	public void setFedSumSpectrumOptions(DAFEventElementOptions fedSumSpectrumOptions) {
		this.fedSumSpectrumOptions = fedSumSpectrumOptions;
	}

	public void setFedSpectrumOptions(List<DAFEventElementOptions> fedSpectrumOptions) {
		this.fedSpectrumOptions = fedSpectrumOptions;
	}

	public void setFedFastCountOptions(List<DAFEventElementOptions> fedFastCountOptions) {
		this.fedFastCountOptions = fedFastCountOptions;
	}

	public void setFedSlowCountOptions(List<DAFEventElementOptions> fedSlowCountOptions) {
		this.fedSlowCountOptions = fedSlowCountOptions;
	}

	public void setFedDeadTimePctOptions(List<DAFEventElementOptions> fedDeadTimePctOptions) {
		this.fedDeadTimePctOptions = fedDeadTimePctOptions;
	}

	public void setFedElapsedRealTimeOptions(List<DAFEventElementOptions> fedElapsedRealTimeOptions) {
		this.fedElapsedRealTimeOptions = fedElapsedRealTimeOptions;
	}

	public void setFedElpasedLiveTimeOptions(List<DAFEventElementOptions> fedElpasedLiveTimeOptions) {
		this.fedElpasedLiveTimeOptions = fedElpasedLiveTimeOptions;
	}

	public void setFedDefaultNChannels(int fedDefaultNChannels) {
		this.fedDefaultNChannels = fedDefaultNChannels;
	}
	
	public void setCustomRecordParsers(Collection<DAFRecordParser> customRecordParsers) {
		this.customRecordParsers = customRecordParsers;
	}
}
