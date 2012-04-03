/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersFromDAFConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.daf.DAFDataParser;
import ca.sciencestudio.data.daf.DAFElement;
import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.daf.DAFRecord;
import ca.sciencestudio.data.daf.DAFRecordParser;
import ca.sciencestudio.data.daf.DAFSpectrumParser;
import ca.sciencestudio.data.daf.DAFSpectrumRecord;
import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.data.support.DataFormatException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 * 
 */
public class MapXYVespersFromDAFConverter extends AbstractMapXYVespersConverter implements StdCategories {
	
	public static final int DEFAULT_SED_NCHANNELS = 2048;
	public static final int DEFAULT_FED_NCHANNELS = 2048;
	
	private static final double MAXIMUM_STEP_ERROR = 0.001;
	
	//private static final String FOUR_ELEMENT_DETECTOR_UID = "FED";
	//private static final String SINGLE_ELEMENT_DETECTOR_UID = "SED";

	/// Parameters to be configured by a ConverterFactory ///
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
	//////////////////////////////////////////////////////////
	
	private int dataRecordIndex = -1;
	private int bkgdRecordIndex = -1;

	private DAFDataParser dafDataParser;
	private DAFSpectrumParser dafSpectraParser;
	
	private int sizeX = 0, sizeY = 0;
	private double stepX = 0.0, stepY = 0.0;
	private double startX = 0.0, startY = 0.0;
	private double endX = 0.0, endY = 0.0;
	
	private int pointX = 0, pointY = 0;
	private double posX = 0.0, posY = 0.0;
	private double nextX = 0.0, nextY = 0.0;
	private double prevX = 0.0, prevY = 0.0;

	private int sedNChannels = -1;
	private int fedNChannels = -1;
	
	protected Log log = LogFactory.getLog(getClass());

	public MapXYVespersFromDAFConverter(String fromFormat, String toFormat, boolean forceUpdate) throws ConverterException {
		super(fromFormat, toFormat, forceUpdate);
	}

	public ConverterMap convert() throws ConverterException {

		if(!hasPositionXY()) {
			throw new ConverterException("Data file does not contain required X and Y positions.");
		}

		try {
			dafDataParser = new DAFDataParser(getDafDataFile(), customRecordParsers);
		}
		catch(Exception e) {
			throw new ConverterException("Exception while creating data parser for file: " + getDafDataFile(), e);
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
				dafSpectraParser = new DAFSpectrumParser(getDafSpectraFile());
			}
			catch(Exception e) {
				throw new ConverterException("Exception while creating spectra parser for file: " + getDafSpectraFile(), e);
			}
		}

		try {
			if(getAdapter().openDestination()) {
				return getResponse();
			}
			
			Date epoch = new Date(0L);
			Date startTime = epoch;
			if((getScanStartDate() != null) && (getScanStartDate().after(epoch))) {
				startTime = getScanStartDate();
			}
			else if((dafDataParser.getStartTime() != null) && (dafDataParser.getStartTime().after(epoch))) {
				startTime = dafDataParser.getStartTime();
			}
			else if((getDafDataFile() != null) && (getDafDataFile().lastModified() > epoch.getTime())) {
				startTime = new Date(getDafDataFile().lastModified());
			}
			getAdapter().recScanStartTime(startTime);
			
			Date endTime = startTime;
			if((getScanEndDate() != null) && (getScanEndDate().after(startTime))) {
				endTime = getScanEndDate();
			}
			getAdapter().recScanEndTime(endTime);
			
			if(hasSSModel10()) {
				getAdapter().recProjectName(getProjectName());
				getAdapter().recSessionName(getSessionName());
				getAdapter().recFacilityName(getFacilityName());
				getAdapter().recLaboratoryName(getLaboratoryName());
				getAdapter().recExperimentName(getExperimentName());
				getAdapter().recInstrumentName(getInstrumentName());
				getAdapter().recTechniqueName(getTechniqueName());
				getAdapter().recScanName(getScanName());
				getAdapter().recSampleName(getSampleName());
			}
			
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
					// nothing to do, maybe log something //
				}
			}

			padMissingPoints();
			
			getAdapter().closeDestination();
		}
		catch(ConverterException e) {
			getAdapter().deleteDestination();
			throw e;
		}
		
		return getResponse();
	}
	
	private void processDataRecord(DAFRecord record) throws ConverterException {
		
		if(dataRecordIndex == 0) {
			
			List<DAFElement> dataEventElements = record.getEvent().getElements();
			
			String name, desc;
			
			if(hasPositionXY()) {
				getAdapter().initSizeX("SizeX", "Number of Points in X");
				getAdapter().initStepX("StepX", "Step Size in X");
				getAdapter().initStartX("StartX", "Start Position in X");
				getAdapter().initEndX("EndX", "End Position in X");
				
				getAdapter().initSizeY("SizeY", "Number of Points in Y");
				getAdapter().initStepY("StepY", "Step Size in Y");
				getAdapter().initStartY("StartY", "Start Position in Y");
				getAdapter().initEndY("EndY", "End Position in Y");
				
				getAdapter().initPointX("PointX", "Computed Point Index in X");
				getAdapter().initPointY("PointY", "Computed Point Index in Y");
				
				if(getPosXFeedbackIdx() >= 0) {
					name = dataEventElements.get(getPosXFeedbackIdx()).getName();
					desc = dataEventElements.get(getPosXFeedbackIdx()).getDescription();
				}
				else {
					name = dataEventElements.get(getPosXSetpointIdx()).getName();
					desc = dataEventElements.get(getPosXSetpointIdx()).getDescription();
				}
				getAdapter().initPositionX(name, desc);

				if(getPosYFeedbackIdx() >= 0) {
					name = dataEventElements.get(getPosYFeedbackIdx()).getName();
					desc = dataEventElements.get(getPosYFeedbackIdx()).getDescription();
				}
				else {
					name = dataEventElements.get(getPosYSetpointIdx()).getName();
					desc = dataEventElements.get(getPosYSetpointIdx()).getDescription();
				}
				getAdapter().initPositionY(name, desc);
			}
			
			if(hasBeamCurrent()) {
				if(getSrCurrentIdx() >= 0) {
					name = dataEventElements.get(getSrCurrentIdx()).getName();
					desc = dataEventElements.get(getSrCurrentIdx()).getDescription();
					getAdapter().initSrCurrent(name, desc);
				}
				
				for(int i=0; i<getMcsCurrentIdx().length; i++) {
					if(getMcsCurrentIdx()[i] >= 0) {
						name = dataEventElements.get(getMcsCurrentIdx()[i]).getName();
						desc = dataEventElements.get(getMcsCurrentIdx()[i]).getDescription();
						getAdapter().initMcsCurrent(i, name, desc);
					}
				}
			}

			if(hasSingleElementDetector()) {
						
				if(getSedFastCountIdx() >= 0) {
					name = dataEventElements.get(getSedFastCountIdx()).getName();
					desc = dataEventElements.get(getSedFastCountIdx()).getDescription();
					getAdapter().initSedFastCount(name, desc);
				}

				if(getSedSlowCountIdx() >= 0) {
					name = dataEventElements.get(getSedSlowCountIdx()).getName();
					desc = dataEventElements.get(getSedSlowCountIdx()).getDescription();
					getAdapter().initSedSlowCount(name, desc);
				}
				
				if(getSedDeadTimePctIdx() >= 0) {
					name = dataEventElements.get(getSedDeadTimePctIdx()).getName();
					desc = dataEventElements.get(getSedDeadTimePctIdx()).getDescription();
					getAdapter().initSedDeadTimePct(name, desc);
				}
				
				if(getSedElapsedRealTimeIdx() >= 0) {
					name = dataEventElements.get(getSedElapsedRealTimeIdx()).getName();
					desc = dataEventElements.get(getSedElapsedRealTimeIdx()).getDescription();
					getAdapter().initSedElapsedRealTime(name, desc);
				}
				
				if(getSedElpasedLiveTimeIdx() >= 0) {
					name = dataEventElements.get(getSedElpasedLiveTimeIdx()).getName();
					desc = dataEventElements.get(getSedElpasedLiveTimeIdx()).getDescription();
					getAdapter().initSedElapsedLiveTime(name, desc);
				}
			
				if(sedNChannels < 0) {
					sedNChannels = getSedDefaultNChannels();
				}
				
				name = dataEventElements.get(getSedSpectrumIdx()).getName();
				desc = dataEventElements.get(getSedSpectrumIdx()).getDescription();
				getAdapter().initSedSpectrum(sedNChannels, name, desc);
				
			}
			else if(hasFourElementDetector()) {
					
				if(allGreaterThanOrEqual(getFedFastCountIdx(), 0)) {
					for(int i=0; i<getFedFastCountIdx().length; i++) {
						name = dataEventElements.get(getFedFastCountIdx()[i]).getName();
						desc = dataEventElements.get(getFedFastCountIdx()[i]).getDescription();
						getAdapter().initFedFastCount(i, name, desc);
					}
				}
			
				if(allGreaterThanOrEqual(getFedSlowCountIdx(), 0)) {
					for(int i=0; i<getFedSlowCountIdx().length; i++) {
						name = dataEventElements.get(getFedSlowCountIdx()[i]).getName();
						desc = dataEventElements.get(getFedSlowCountIdx()[i]).getDescription();
						getAdapter().initFedSlowCount(i, name, desc);
					}
				}
				
				if(allGreaterThanOrEqual(getFedDeadTimePctIdx(), 0)) {
					for(int i=0; i<getFedDeadTimePctIdx().length; i++) {
						name = dataEventElements.get(getFedDeadTimePctIdx()[i]).getName();
						desc = dataEventElements.get(getFedDeadTimePctIdx()[i]).getDescription();
						getAdapter().initFedDeadTimePct(i, name, desc);
					}
				}
					
				if(allGreaterThanOrEqual(getFedElapsedRealTimeIdx(), 0)) {
					for(int i=0; i<getFedElapsedRealTimeIdx().length; i++) {
						name = dataEventElements.get(getFedElapsedRealTimeIdx()[i]).getName();
						desc = dataEventElements.get(getFedElapsedRealTimeIdx()[i]).getDescription();
						getAdapter().initFedElapsedRealTime(i, name, desc);
					}
				}
				
				if(allGreaterThanOrEqual(getFedElpasedLiveTimeIdx(), 0)) {
					for(int i=0; i<getFedElpasedLiveTimeIdx().length; i++) {
						name = dataEventElements.get(getFedElpasedLiveTimeIdx()[i]).getName();
						desc = dataEventElements.get(getFedElpasedLiveTimeIdx()[i]).getDescription();
						getAdapter().initFedElapsedLiveTime(i, name, desc);
					}
				}
							
				if(fedNChannels < 0) {
					fedNChannels = getFedDefaultNChannels();
				}
			
				if(getFedSumSpectrumIdx() >= 0) {
					name = dataEventElements.get(getFedSumSpectrumIdx()).getName();
					desc = dataEventElements.get(getFedSumSpectrumIdx()).getDescription();
					getAdapter().initFedSumSpectrum(fedNChannels, name, desc);
				}
			
				if(allGreaterThanOrEqual(getFedSpectrumIdx(), 0)) {
					for(int i=0; i<getFedSpectrumIdx().length; i++) {
						name = dataEventElements.get(getFedSpectrumIdx()[i]).getName();
						desc = dataEventElements.get(getFedSpectrumIdx()[i]).getDescription();
						getAdapter().initFedSpectrum(i, fedNChannels, name, desc);
					}
				}
			}
		}
		
		getAdapter().openDataRecord(dataRecordIndex);
		
		try {
			if(dataRecordIndex == 0) {
				prevX = record.getDouble(getPosXSetpointIdx());
				prevY = record.getDouble(getPosYSetpointIdx());
				nextX = prevX;
				nextY = prevY;
				endX = prevX;
				getAdapter().recEndX(endX);
				endY = prevY;
				getAdapter().recEndY(endY);
				startX = prevX;
				getAdapter().recStartX(startX);
				startY = prevY;
				getAdapter().recStartY(startY);
				sizeX = 1;
				getAdapter().recSizeX(sizeX);
				sizeY = 1;
				getAdapter().recSizeY(sizeY);
			}
			else {
				prevX = nextX;
				prevY = nextY;
				nextX = record.getDouble(getPosXSetpointIdx());
				nextY = record.getDouble(getPosYSetpointIdx());
		
				if(nextX != prevX) {
					if(nextX == startX) {
						pointX = 0;
					}
					else {
						pointX++;
						if(pointX == sizeX) {
							endX = nextX;
							getAdapter().recEndX(endX);
							sizeX += 1;
							getAdapter().recSizeX(sizeX);
						}
						
						if(stepX == 0.0) {
							stepX = nextX - prevX;
						}
						else {
							double delta = nextX - prevX;
							if(equal(stepX, delta, MAXIMUM_STEP_ERROR)) {
								stepX = (stepX + delta) / 2.0;
							}
							else if(equalSignum(stepX, delta)) {
								if(log.isDebugEnabled()) {
									log.debug("Data point X-position step: " + delta + ", expecting: " + stepX + " at index: " + dataRecordIndex + ", will ignore.");
								}
							}
							else {
								throw new ConverterException("Data point X-position step error: " + delta + ", expecting: " + stepX + " at index: " + dataRecordIndex);
							}
						}
						getAdapter().recStepX(stepX);
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
							getAdapter().recEndY(endY);
							sizeY += 1;
							getAdapter().recSizeY(sizeY);
						}
						
						if(stepY == 0.0) {
							stepY = nextY - prevY;
						}
						else {
							double delta = nextY - prevY;
							if(equal(stepY, delta, MAXIMUM_STEP_ERROR)) {
								stepY = (stepY + delta) / 2.0;
							}
							else if(equalSignum(stepY, delta)) {
								if(log.isDebugEnabled()) {
									log.debug("Data point Y-position step: " + delta + ", expecting: " + stepY + " at index: " + dataRecordIndex + ", will ignore.");
								}
							}
							else {
								throw new ConverterException("Data point Y-position step error: " + delta + ", expecting: " + stepY + " at index: " + dataRecordIndex);
							}
						}
						getAdapter().recStepY(stepY);
					}
				}
			}
		}
		catch(RecordFormatException e) {
			throw new ConverterException("Record format exception getting setpoint from data record with index: " + dataRecordIndex, e);
		}
		
		// MapXY11 Category //

		getAdapter().recPointX(pointX);
		getAdapter().recPointY(pointY);
		
		try {
			posX = record.getDouble(getPosXFeedbackIdx());
		}
		catch (RecordFormatException e) {
			posX = nextX; // Fall-back to setpointX //
		}
		getAdapter().recPositionX(posX);

		try {
			posY = record.getDouble(getPosYFeedbackIdx());
		}
		catch (RecordFormatException e) {
			posY = nextY; // Fall-back to setpointY //
		}
		getAdapter().recPositionY(posY);

		
		// BeamI11 Category //
		
		if(hasBeamCurrent()) {
			try {
				if(getSrCurrentIdx() >= 0) {
					getAdapter().recSrCurrent(record.getDouble(getSrCurrentIdx()));
				}

				for(int i=0; i<getMcsCurrentIdx().length; i++) {
					if(getMcsCurrentIdx()[i] >= 0) {
						getAdapter().recMcsCurrent(i, record.getDouble(getMcsCurrentIdx()[i]));
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
				if(getSedFastCountIdx() >= 0) {
					getAdapter().recSedFastCount(record.getLong(getSedFastCountIdx()));
				}
				
				if(getSedSlowCountIdx() >= 0) {
					getAdapter().recSedSlowCount(record.getLong(getSedSlowCountIdx()));
				}
				
				if(getSedDeadTimePctIdx() >= 0) {
					getAdapter().recSedDeadTimePct(record.getDouble(getSedDeadTimePctIdx()));
				}
			
				if(getSedElapsedRealTimeIdx() >= 0) {
					getAdapter().recSedElapsedRealTime(record.getDouble(getSedElapsedRealTimeIdx()));
				}
			
				if(getSedElpasedLiveTimeIdx() >= 0) {
					getAdapter().recSedElapsedLiveTime(record.getDouble(getSedElpasedLiveTimeIdx()));
				}
				
				long sedSpectrumOffset = record.getLong(getSedSpectrumIdx());
				DAFSpectrumRecord specRecord = dafSpectraParser.parseRecord(sedSpectrumOffset, sedNChannels);
				getAdapter().recSedSpectrum(specRecord.getLongArray());
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
				if(allGreaterThanOrEqual(fedFastCountIdx, 0)) {
					for(int i=0; i<fedFastCountIdx.length; i++) {
						getAdapter().recFedFastCount(i, record.getLong(fedFastCountIdx[i]));
					}
				}
				
				if(allGreaterThanOrEqual(fedSlowCountIdx, 0)) {
					for(int i=0; i<fedSlowCountIdx.length; i++) {
						getAdapter().recFedSlowCount(i, record.getLong(fedSlowCountIdx[i]));
					}
				}
			
				if(allGreaterThanOrEqual(fedDeadTimePctIdx, 0)) {
					for(int i=0; i<fedDeadTimePctIdx.length; i++) {
						getAdapter().recFedDeadTimePct(i, record.getDouble(fedDeadTimePctIdx[i]));
					}
				}
				
				if(allGreaterThanOrEqual(fedElapsedRealTimeIdx, 0)) {
					for(int i=0; i<fedElapsedRealTimeIdx.length; i++) {
						getAdapter().recFedElapsedRealTime(i, record.getDouble(fedElapsedRealTimeIdx[i]));
					}
				}
			
				if(allGreaterThanOrEqual(fedElpasedLiveTimeIdx, 0)) {
					for(int i=0; i<fedElpasedLiveTimeIdx.length; i++) {
						getAdapter().recFedElapsedLiveTime(i, record.getDouble(fedElpasedLiveTimeIdx[i]));
					}
				}
			
				long fedSpecOffset;
				DAFSpectrumRecord fedSpecRecord;
			
				if(fedSumSpectrumIdx >= 0) {
					fedSpecOffset = record.getLong(fedSumSpectrumIdx);
					fedSpecRecord = dafSpectraParser.parseRecord(fedSpecOffset, fedNChannels);
					getAdapter().recFedSumSpectrum(fedSpecRecord.getLongArray());
				}
				
				if(allGreaterThanOrEqual(fedSpectrumIdx, 0)) {
					for(int i=0; i<fedSpectrumIdx.length; i++) {
						fedSpecOffset = record.getLong(fedSpectrumIdx[i]);
						fedSpecRecord = dafSpectraParser.parseRecord(fedSpecOffset, fedNChannels);
						getAdapter().recFedSpectrum(i, fedSpecRecord.getLongArray());
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
	
		getAdapter().closeDataRecord(dataRecordIndex);
	}
	
	private void processBkgdRecord(DAFRecord record) throws ConverterException {

		if(bkgdRecordIndex == 0) {
			
			List<DAFElement> bkgdEventElements = record.getEvent().getElements();
			
			String name, desc;
			
			if(hasSingleElementDetector()) {
				
				if((sedNChannels < 0) && (sedNChannelsIdx >= 0)) {
					try {
						sedNChannels = record.getInt(sedNChannelsIdx);
					}
					catch (RecordFormatException e) {
						sedNChannels = getSedDefaultNChannels();
					}
				}
				
				if(sedMaxEnergyIdx >= 0) {
					try {
						name = bkgdEventElements.get(sedMaxEnergyIdx).getName();
						desc = bkgdEventElements.get(sedMaxEnergyIdx).getDescription();
						getAdapter().initSedMaxEnergy(name, desc);
						getAdapter().recSedMaxEnergy(record.getDouble(sedMaxEnergyIdx) * 1000 /* Convert keV to eV */);
					}
					catch (RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedEnergyGapTimeIdx >= 0) {
					try {
						name = bkgdEventElements.get(sedEnergyGapTimeIdx).getName();
						desc = bkgdEventElements.get(sedEnergyGapTimeIdx).getDescription();
						getAdapter().initSedEnergyGapTime(name, desc);
						getAdapter().recSedEnergyGapTime(record.getDouble(sedEnergyGapTimeIdx));
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedEnergyPeakingTimeIdx >= 0) {
					try {
						name = bkgdEventElements.get(sedEnergyPeakingTimeIdx).getName();
						desc = bkgdEventElements.get(sedEnergyPeakingTimeIdx).getDescription();
						getAdapter().initSedEnergyPeakingTime(name, desc);
						getAdapter().recSedEnergyPeakingTime(record.getDouble(sedEnergyPeakingTimeIdx));
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedPresetRealTimeIdx >= 0) {
					try {
						name = bkgdEventElements.get(sedPresetRealTimeIdx).getName();
						desc = bkgdEventElements.get(sedPresetRealTimeIdx).getDescription();
						getAdapter().initSedPresetRealTime(name, desc);
						getAdapter().recSedPresetRealTime(record.getDouble(sedPresetRealTimeIdx));
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
						fedNChannels = getFedDefaultNChannels();
					}
				}
				
				if(fedMaxEnergyIdx >= 0) {
					try {
						name = bkgdEventElements.get(fedMaxEnergyIdx).getName();
						desc = bkgdEventElements.get(fedMaxEnergyIdx).getDescription();
						getAdapter().initFedMaxEnergy(name, desc);
						getAdapter().recFedMaxEnergy(record.getDouble(fedMaxEnergyIdx) * 1000 /* Convert keV to eV */);
					}
					catch (RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedEnergyGapTimeIdx >= 0) {
					try {
						name = bkgdEventElements.get(fedEnergyGapTimeIdx).getName();
						desc = bkgdEventElements.get(fedEnergyGapTimeIdx).getDescription();
						getAdapter().initFedEnergyGapTime(name, desc);
						getAdapter().recFedEnergyGapTime(record.getDouble(fedEnergyGapTimeIdx));
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedEnergyPeakingTimeIdx >= 0) {
					try {
						name = bkgdEventElements.get(fedEnergyPeakingTimeIdx).getName();
						desc = bkgdEventElements.get(fedEnergyPeakingTimeIdx).getDescription();
						getAdapter().initFedEnergyPeakingTime(name, desc);
						getAdapter().recFedEnergyPeakingTime(record.getDouble(fedEnergyPeakingTimeIdx));
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedPresetRealTimeIdx >= 0) {
					try {
						name = bkgdEventElements.get(fedPresetRealTimeIdx).getName();
						desc = bkgdEventElements.get(fedPresetRealTimeIdx).getDescription();
						getAdapter().initFedPresetRealTime(name, desc);
						getAdapter().recFedPresetRealTime(record.getDouble(fedPresetRealTimeIdx));
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
			}
		}
	}

	private void padMissingPoints() throws ConverterException {
		
		int index;
		int padX = sizeX - pointX;
		int padY = sizeY - pointY;
		
		if(padX > 1) {
			for(int i = 1; i < padX; i++) {
				index = dataRecordIndex + i;
				getAdapter().openDataRecord(index);
				getAdapter().recPointX(pointX + i);
				getAdapter().recPositionX(posX + (i * stepX));
				getAdapter().closeDataRecord(index);
			}
		}
		else if(padY > 1) {
			for(int i = 1; i < padY; i++) {
				index = dataRecordIndex + i;
				getAdapter().openDataRecord(index);
				getAdapter().recPointY(pointY + i);
				getAdapter().recPositionY(posY + (i * stepY));
				getAdapter().closeDataRecord(index);
			}
		}
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
		return (dataEventId >= 0) && (sedSpectrumIdx >= 0);
	}

	protected boolean hasFourElementDetector() {
		return (dataEventId >= 0) && ((fedSumSpectrumIdx >= 0) || 
					allGreaterThanOrEqual(fedSpectrumIdx, 0));
	}

	public File getDafDataFile() {
		return dafDataFile;
	}
	public void setDafDataFile(File dafDataFile) {
		this.dafDataFile = dafDataFile;
	}

	public File getDafSpectraFile() {
		return dafSpectraFile;
	}
	public void setDafSpectraFile(File dafSpectraFile) {
		this.dafSpectraFile = dafSpectraFile;
	}

	public int getDataEventId() {
		return dataEventId;
	}
	public void setDataEventId(int dataEventId) {
		this.dataEventId = dataEventId;
	}

	public int getBkgdEventId() {
		return bkgdEventId;
	}
	public void setBkgdEventId(int bkgdEventId) {
		this.bkgdEventId = bkgdEventId;
	}

	public int getPosXSetpointIdx() {
		return posXSetpointIdx;
	}
	public void setPosXSetpointIdx(int posXSetpointIdx) {
		this.posXSetpointIdx = posXSetpointIdx;
	}

	public int getPosYSetpointIdx() {
		return posYSetpointIdx;
	}
	public void setPosYSetpointIdx(int posYSetpointIdx) {
		this.posYSetpointIdx = posYSetpointIdx;
	}

	public int getPosXFeedbackIdx() {
		return posXFeedbackIdx;
	}
	public void setPosXFeedbackIdx(int posXFeedbackIdx) {
		this.posXFeedbackIdx = posXFeedbackIdx;
	}

	public int getPosYFeedbackIdx() {
		return posYFeedbackIdx;
	}
	public void setPosYFeedbackIdx(int posYFeedbackIdx) {
		this.posYFeedbackIdx = posYFeedbackIdx;
	}

	public int getSrCurrentIdx() {
		return srCurrentIdx;
	}
	public void setSrCurrentIdx(int srCurrentIdx) {
		this.srCurrentIdx = srCurrentIdx;
	}

	public int[] getMcsCurrentIdx() {
		return mcsCurrentIdx;
	}
	public void setMcsCurrentIdx(int[] mcsCurrentIdx) {
		this.mcsCurrentIdx = mcsCurrentIdx;
	}

	public int getSedNChannelsIdx() {
		return sedNChannelsIdx;
	}
	public void setSedNChannelsIdx(int sedNChannelsIdx) {
		this.sedNChannelsIdx = sedNChannelsIdx;
	}

	public int getSedMaxEnergyIdx() {
		return sedMaxEnergyIdx;
	}
	public void setSedMaxEnergyIdx(int sedMaxEnergyIdx) {
		this.sedMaxEnergyIdx = sedMaxEnergyIdx;
	}

	public int getSedEnergyGapTimeIdx() {
		return sedEnergyGapTimeIdx;
	}
	public void setSedEnergyGapTimeIdx(int sedEnergyGapTimeIdx) {
		this.sedEnergyGapTimeIdx = sedEnergyGapTimeIdx;
	}

	public int getSedPresetRealTimeIdx() {
		return sedPresetRealTimeIdx;
	}
	public void setSedPresetRealTimeIdx(int sedPresetRealTimeIdx) {
		this.sedPresetRealTimeIdx = sedPresetRealTimeIdx;
	}

	public int getSedEnergyPeakingTimeIdx() {
		return sedEnergyPeakingTimeIdx;
	}
	public void setSedEnergyPeakingTimeIdx(int sedEnergyPeakingTimeIdx) {
		this.sedEnergyPeakingTimeIdx = sedEnergyPeakingTimeIdx;
	}

	public int getSedSpectrumIdx() {
		return sedSpectrumIdx;
	}
	public void setSedSpectrumIdx(int sedSpectrumIdx) {
		this.sedSpectrumIdx = sedSpectrumIdx;
	}

	public int getSedFastCountIdx() {
		return sedFastCountIdx;
	}
	public void setSedFastCountIdx(int sedFastCountIdx) {
		this.sedFastCountIdx = sedFastCountIdx;
	}

	public int getSedSlowCountIdx() {
		return sedSlowCountIdx;
	}
	public void setSedSlowCountIdx(int sedSlowCountIdx) {
		this.sedSlowCountIdx = sedSlowCountIdx;
	}

	public int getSedDeadTimePctIdx() {
		return sedDeadTimePctIdx;
	}
	public void setSedDeadTimePctIdx(int sedDeadTimePctIdx) {
		this.sedDeadTimePctIdx = sedDeadTimePctIdx;
	}

	public int getSedElapsedRealTimeIdx() {
		return sedElapsedRealTimeIdx;
	}
	public void setSedElapsedRealTimeIdx(int sedElapsedRealTimeIdx) {
		this.sedElapsedRealTimeIdx = sedElapsedRealTimeIdx;
	}

	public int getSedElpasedLiveTimeIdx() {
		return sedElpasedLiveTimeIdx;
	}
	public void setSedElpasedLiveTimeIdx(int sedElpasedLiveTimeIdx) {
		this.sedElpasedLiveTimeIdx = sedElpasedLiveTimeIdx;
	}
	
	public int getSedDefaultNChannels() {
		return sedDefaultNChannels;
	}
	public void setSedDefaultNChannels(int sedDefaultNChannels) {
		this.sedDefaultNChannels = sedDefaultNChannels;
	}
	
	public int getFedNChannelsIdx() {
		return fedNChannelsIdx;
	}
	public void setFedNChannelsIdx(int fedNChannelsIdx) {
		this.fedNChannelsIdx = fedNChannelsIdx;
	}

	public int getFedMaxEnergyIdx() {
		return fedMaxEnergyIdx;
	}
	public void setFedMaxEnergyIdx(int fedMaxEnergyIdx) {
		this.fedMaxEnergyIdx = fedMaxEnergyIdx;
	}

	public int getFedEnergyGapTimeIdx() {
		return fedEnergyGapTimeIdx;
	}
	public void setFedEnergyGapTimeIdx(int fedEnergyGapTimeIdx) {
		this.fedEnergyGapTimeIdx = fedEnergyGapTimeIdx;
	}

	public int getFedPresetRealTimeIdx() {
		return fedPresetRealTimeIdx;
	}
	public void setFedPresetRealTimeIdx(int fedPresetRealTimeIdx) {
		this.fedPresetRealTimeIdx = fedPresetRealTimeIdx;
	}
	
	public int getFedEnergyPeakingTimeIdx() {
		return fedEnergyPeakingTimeIdx;
	}
	public void setFedEnergyPeakingTimeIdx(int fedEnergyPeakingTimeIdx) {
		this.fedEnergyPeakingTimeIdx = fedEnergyPeakingTimeIdx;
	}

	public int getFedSumSpectrumIdx() {
		return fedSumSpectrumIdx;
	}
	public void setFedSumSpectrumIdx(int fedSumSpectrumIdx) {
		this.fedSumSpectrumIdx = fedSumSpectrumIdx;
	}

	public int[] getFedSpectrumIdx() {
		return fedSpectrumIdx;
	}
	public void setFedSpectrumIdx(int[] fedSpectrumIdx) {
		this.fedSpectrumIdx = fedSpectrumIdx;
	}

	public int[] getFedFastCountIdx() {
		return fedFastCountIdx;
	}
	public void setFedFastCountIdx(int[] fedFastCountIdx) {
		this.fedFastCountIdx = fedFastCountIdx;
	}

	public int[] getFedSlowCountIdx() {
		return fedSlowCountIdx;
	}
	public void setFedSlowCountIdx(int[] fedSlowCountIdx) {
		this.fedSlowCountIdx = fedSlowCountIdx;
	}
	
	public int[] getFedDeadTimePctIdx() {
		return fedDeadTimePctIdx;
	}
	public void setFedDeadTimePctIdx(int[] fedDeadTimePctIdx) {
		this.fedDeadTimePctIdx = fedDeadTimePctIdx;
	}
	
	public int[] getFedElapsedRealTimeIdx() {
		return fedElapsedRealTimeIdx;
	}
	public void setFedElapsedRealTimeIdx(int[] fedElapsedRealTimeIdx) {
		this.fedElapsedRealTimeIdx = fedElapsedRealTimeIdx;
	}

	public int[] getFedElpasedLiveTimeIdx() {
		return fedElpasedLiveTimeIdx;
	}
	public void setFedElpasedLiveTimeIdx(int[] fedElpasedLiveTimeIdx) {
		this.fedElpasedLiveTimeIdx = fedElpasedLiveTimeIdx;
	}

	public int getFedDefaultNChannels() {
		return fedDefaultNChannels;
	}
	public void setFedDefaultNChannels(int fedDefaultNChannels) {
		this.fedDefaultNChannels = fedDefaultNChannels;
	}

	public Collection<DAFRecordParser> getCustomRecordParsers() {
		return customRecordParsers;
	}
	public void setCustomRecordParsers(Collection<DAFRecordParser> customRecordParsers) {
		this.customRecordParsers = customRecordParsers;
	}
}
