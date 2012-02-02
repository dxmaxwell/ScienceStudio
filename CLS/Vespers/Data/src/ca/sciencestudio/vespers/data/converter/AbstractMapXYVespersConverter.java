/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapXYVespersConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import java.io.File;
import java.io.IOException;
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

		try {
			if(openDestination(dafDataParser, dafSpectraParser)) {
				return getResponse();
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
			
			closeDestination();
		}
		catch(ConverterException e) {
			deleteDestination();
			throw e;
		}
		
		return getResponse();
	}

	protected abstract boolean openDestination(DAFDataParser dafDataParser, DAFSpectrumParser dafSpectraParser) throws ConverterException;
	protected abstract void closeDestination() throws ConverterException;
	protected abstract void deleteDestination() throws ConverterException;
	
	private void processDataRecord(DAFRecord record) throws ConverterException {
		
		if(sedNChannels < 0) {
			sedNChannels = sedDefaultNChannels;
		}
		
		if(fedNChannels < 0) {
			fedNChannels = fedDefaultNChannels;
		}
		
		openDataRecord(dataRecordIndex, record, sedNChannels, fedNChannels);
		
		try {
			if(dataRecordIndex == 0) {
				prevX = record.getDouble(posXSetpointIdx);
				prevY = record.getDouble(posYSetpointIdx);
				nextX = prevX;
				nextY = prevY;
				endX = prevX;
				recEndX(endX);
				endY = prevY;
				recEndY(endY);
				startX = prevX;
				recStartX(startX);
				startY = prevY;
				recStartY(startY);
				sizeX = 1;
				recSizeX(sizeX);
				sizeY = 1;
				recSizeY(sizeY);
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
							recEndX(endX);
							sizeX += 1;
							recSizeX(sizeX);
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
						recStepX(stepX);
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
							recEndY(endY);
							sizeY += 1;
							recSizeY(sizeY);
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
						recStepY(stepY);
					}
				}
			}
		}
		catch(RecordFormatException e) {
			throw new ConverterException("Record format exception getting setpoint from data record with index: " + dataRecordIndex, e);
		}
		
		// MapXY11 Category //

		recPointX(pointX);
		recPointY(pointY);
		
		try {
			posX = record.getDouble(posXFeedbackIdx);
		}
		catch (RecordFormatException e) {
			posX = nextX; // Fall-back to setpointX //
		}
		recPositionX(posX);

		try {
			posY = record.getDouble(posYFeedbackIdx);
		}
		catch (RecordFormatException e) {
			posY = nextY; // Fall-back to setpointY //
		}
		recPositionY(posY);

		
		// BeamI11 Category //
		
		if(hasBeamCurrent()) {
			try {
				if(srCurrentIdx >= 0) {
					recSRCurrent(record.getDouble(srCurrentIdx));
				}

				for(int i=0; i<mcsCurrentIdx.length; i++) {
					if(mcsCurrentIdx[i] >= 0) {
						recMCSCurrent(i, record.getDouble(mcsCurrentIdx[i]));
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
				if(sedFastCountIdx >= 0) {
					recSedFastCount(record.getLong(sedFastCountIdx));
				}
				
				if(sedSlowCountIdx >= 0) {
					recSedSlowCount(record.getLong(sedSlowCountIdx));
				}
				
				if(sedDeadTimePctIdx >= 0) {
					recSedDeadTimePct(record.getDouble(sedDeadTimePctIdx));
				}
			
				if(sedElapsedRealTimeIdx >= 0) {
					recSedElapsedRealTime(record.getDouble(sedElapsedRealTimeIdx));
				}
			
				if(sedElpasedLiveTimeIdx >= 0) {
					recSedElapsedLiveTime(record.getDouble(sedElpasedLiveTimeIdx));
				}
				
				long sedSpectrumOffset = record.getLong(sedSpectrumIdx);
				DAFSpectrumRecord specRecord = dafSpectraParser.parseRecord(sedSpectrumOffset, sedNChannels);
				recSedSpectrum(specRecord.getLongArray());
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
						recFedFastCount(i, record.getLong(fedFastCountIdx[i]));
					}
				}
				
				if(allGreaterThanOrEqual(fedSlowCountIdx, 0)) {
					for(int i=0; i<fedSlowCountIdx.length; i++) {
						recFedSlowCount(i, record.getLong(fedSlowCountIdx[i]));
					}
				}
			
				if(allGreaterThanOrEqual(fedDeadTimePctIdx, 0)) {
					for(int i=0; i<fedDeadTimePctIdx.length; i++) {
						recFedDeadTimePct(i, record.getDouble(fedDeadTimePctIdx[i]));
					}
				}
				
				if(allGreaterThanOrEqual(fedElapsedRealTimeIdx, 0)) {
					for(int i=0; i<fedElapsedRealTimeIdx.length; i++) {
						recFedElapsedRealTime(i, record.getDouble(fedElapsedRealTimeIdx[i]));
					}
				}
			
				if(allGreaterThanOrEqual(fedElpasedLiveTimeIdx, 0)) {
					for(int i=0; i<fedElpasedLiveTimeIdx.length; i++) {
						recFedElapsedLiveTime(i, record.getDouble(fedElpasedLiveTimeIdx[i]));
					}
				}
			
				long fedSpecOffset;
				DAFSpectrumRecord fedSpecRecord;
			
				if(fedSumSpectrumIdx >= 0) {
					fedSpecOffset = record.getLong(fedSumSpectrumIdx);
					fedSpecRecord = dafSpectraParser.parseRecord(fedSpecOffset, fedNChannels);
					recFedSumSpectrum(fedSpecRecord.getLongArray());
				}
				
				if(allGreaterThanOrEqual(fedSpectrumIdx, 0)) {
					for(int i=0; i<fedSpectrumIdx.length; i++) {
						fedSpecOffset = record.getLong(fedSpectrumIdx[i]);
						fedSpecRecord = dafSpectraParser.parseRecord(fedSpecOffset, fedNChannels);
						recFedSpectrum(i, fedSpecRecord.getLongArray());
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
	
		closeDataRecord(dataRecordIndex);
	}
	
	protected abstract void openDataRecord(int dataRecordIndex, DAFRecord record, int sedNChannels, int fedNChannels) throws ConverterException;
	protected abstract void closeDataRecord(int dataRecordIndex) throws ConverterException;
	
	// Dimensions X //
	
	protected abstract void recSizeX(int sizeX) throws ConverterException;
	protected abstract void recStartX(double startX) throws ConverterException;
	protected abstract void recEndX(double endX) throws ConverterException;
	protected abstract void recStepX(double stepX) throws ConverterException;
	protected abstract void recPointX(int pointX) throws ConverterException;
	protected abstract void recPositionX(double positionX) throws ConverterException;
	
	// Dimensions Y //

	protected abstract void recSizeY(int sizeY) throws ConverterException;
	protected abstract void recStartY(double startY) throws ConverterException;
	protected abstract void recEndY(double endY) throws ConverterException;
	protected abstract void recStepY(double stepY) throws ConverterException;
	protected abstract void recPointY(int pointY) throws ConverterException;
	protected abstract void recPositionY(double positionY) throws ConverterException;
	
	// Beam Current //
	
	protected abstract void recSRCurrent(double srCurrent) throws ConverterException;
	protected abstract void recMCSCurrent(int index, double mcsCurrent) throws ConverterException;
	
	// SED Detector //
	
	protected abstract void recSedFastCount(long sedFastCount) throws ConverterException;
	protected abstract void recSedSlowCount(long sedSlowCount) throws ConverterException;
	protected abstract void recSedDeadTimePct(double sedDeadTimePct) throws ConverterException;
	protected abstract void recSedElapsedRealTime(double sedElapsedRealTime) throws ConverterException;
	protected abstract void recSedElapsedLiveTime(double sedElapsedLiveTime) throws ConverterException;
	protected abstract void recSedSpectrum(long sedSpectrum[]) throws ConverterException;
	
	// FED Detector //
	
	protected abstract void recFedFastCount(int index, long fedFastCount) throws ConverterException;
	protected abstract void recFedSlowCount(int index, long fedSlowCount) throws ConverterException;
	protected abstract void recFedDeadTimePct(int index, double fedDeadTimePct) throws ConverterException;
	protected abstract void recFedElapsedRealTime(int index, double fedElapsedRealTime) throws ConverterException;
	protected abstract void recFedElapsedLiveTime(int index, double fedElapsedLiveTime) throws ConverterException;
	protected abstract void recFedSpectrum(int index, long fedSpectrum[]) throws ConverterException;
	protected abstract void recFedSumSpectrum(long fedSpectrum[]) throws ConverterException;
	
	private void processBkgdRecord(DAFRecord record) throws ConverterException {

		if(bkgdRecordIndex == 0) {
			
			openBkgdRecord(bkgdRecordIndex);
			
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
						String units = StdUnits.EVOLT;
						String name = bkgdEventElements.get(sedMaxEnergyIdx).getName();
						String description = bkgdEventElements.get(sedMaxEnergyIdx).getDescription();
						recSedMaxEnergy(record.getDouble(sedMaxEnergyIdx) * 1000 /* Convert keV to eV */, units, name, description);
					}
					catch (RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedEnergyGapTimeIdx >= 0) {
					try {
						String units = StdUnits.MICRO_SECOND;
						String name = bkgdEventElements.get(sedEnergyGapTimeIdx).getName();
						String description = bkgdEventElements.get(sedEnergyGapTimeIdx).getDescription();
						recSedEnergyGapTime(record.getDouble(sedEnergyGapTimeIdx), units, name, description);
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedEnergyPeakingTimeIdx >= 0) {
					try {
						String units = StdUnits.MICRO_SECOND;
						String name = bkgdEventElements.get(sedEnergyPeakingTimeIdx).getName();
						String description = bkgdEventElements.get(sedEnergyPeakingTimeIdx).getDescription();
						recSedEnergyPeakingTime(record.getDouble(sedEnergyPeakingTimeIdx), units, name, description);
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(sedPresetRealTimeIdx >= 0) {
					try {
						String units = StdUnits.SECOND;
						String name = bkgdEventElements.get(sedPresetRealTimeIdx).getName();
						String description = bkgdEventElements.get(sedPresetRealTimeIdx).getDescription();
						recSedPresetReadTime(record.getDouble(sedPresetRealTimeIdx), units, name, description);
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
						String units = StdUnits.EVOLT;
						String name = bkgdEventElements.get(fedMaxEnergyIdx).getName();
						String description = bkgdEventElements.get(fedMaxEnergyIdx).getDescription();
						recFedMaxEnergy(record.getDouble(fedMaxEnergyIdx) * 1000 /* Convert keV to eV */, units, name, description);
					}
					catch (RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedEnergyGapTimeIdx >= 0) {
					try {
						String units = StdUnits.MICRO_SECOND;
						String name = bkgdEventElements.get(fedEnergyGapTimeIdx).getName();
						String description = bkgdEventElements.get(fedEnergyGapTimeIdx).getDescription();
						recFedEnergyGapTime(record.getDouble(fedEnergyGapTimeIdx), units, name, description);
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedEnergyPeakingTimeIdx >= 0) {
					try {
						String units = StdUnits.MICRO_SECOND;
						String name = bkgdEventElements.get(fedEnergyPeakingTimeIdx).getName();
						String description = bkgdEventElements.get(fedEnergyPeakingTimeIdx).getDescription();
						recFedEnergyPeakingTime(record.getDouble(fedEnergyPeakingTimeIdx), units, name, description);
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
				
				if(fedPresetRealTimeIdx >= 0) {
					try {
						String units = StdUnits.SECOND;
						String name = bkgdEventElements.get(fedPresetRealTimeIdx).getName();
						String description = bkgdEventElements.get(fedPresetRealTimeIdx).getDescription();
						recFedPresetRealTime(record.getDouble(fedPresetRealTimeIdx), units, name, description);
					}
					catch(RecordFormatException e) {
						// nothing to do //
					}
				}
			}
			
			closeBkgdRecord(bkgdRecordIndex);
		}
	}

	protected abstract void openBkgdRecord(int bkgdRecordIndex);
	protected abstract void closeBkgdRecord(int bkgdRecordIndex);
	
	// SED Detector //
	
	protected abstract void recSedMaxEnergy(double sedMaxEnergy, String units, String name, String description) throws ConverterException;
	protected abstract void recSedEnergyGapTime(double sedEnergyGapTime, String units, String name, String description) throws ConverterException;
	protected abstract void recSedEnergyPeakingTime(double sedEnergyPeakingTime, String units, String name, String description) throws ConverterException;
	protected abstract void recSedPresetReadTime(double sedPresetRealTime, String units, String name, String description) throws ConverterException;
	
	// FED Detector //
	
	protected abstract void recFedMaxEnergy(double fedMaxEnergy, String units, String name, String description) throws ConverterException;
	protected abstract void recFedEnergyGapTime(double fedEnergyGapTime, String units, String name, String description) throws ConverterException;
	protected abstract void recFedEnergyPeakingTime(double fedEnergyPeakingTime, String units, String name, String description) throws ConverterException;
	protected abstract void recFedPresetRealTime(double fedPresetRealTime, String units, String name, String description) throws ConverterException;

	private void padMissingPoints() throws ConverterException {
		
		int index;
		int padX = sizeX - pointX;
		int padY = sizeY - pointY;
		
		if(padX > 1) {
			for(int i = 1; i < padX; i++) {
				index = dataRecordIndex + i;
				openPadRecord(index);
				recPointX(pointX + i);
				recPositionX(posX + (i * stepX));
				closePadRecord(index);
			}
		}
		else if(padY > 1) {
			for(int i = 1; i < padY; i++) {
				index = dataRecordIndex + i;
				openPadRecord(index);
				recPointY(pointY + i);
				recPositionY(posY + (i * stepY));
				closePadRecord(index);
			}
		}
	}

	protected abstract void openPadRecord(int dataRecordIndex) throws ConverterException;
	protected abstract void closePadRecord(int dataRecordIndex) throws ConverterException;
	
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
