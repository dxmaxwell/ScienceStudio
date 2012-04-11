/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersFromAMFConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterException;

/**
 * 
 * @author maxweld
 *
 */
public class MapXYVespersFromAMFConverter extends AbstractMapXYVespersConverter {

	private static final Pattern AMF_DATA_FILE_BLANKLINE_PATTERN = Pattern.compile("\\s*");
	
	private static final Pattern AMF_DATA_FILE_HEADER_PATTERN = Pattern.compile("\\s*#(.*)");
	
	private static final Pattern AMF_DATA_FILE_RECORD_PATTERN = Pattern.compile("\\s*((\\S+\\s+)*\\S+)\\s*");
	
	private static final String AMF_DATA_SPLIT_RECORD_REGEX = "\\s+";
	
	private static final Pattern AMF_SPECTRA_FILE_BLANKLINE_PATTERN = Pattern.compile("\\s*");
	
	private static final Pattern AMF_SPECTRA_FILE_HEADER_PATTERN = AMF_DATA_FILE_HEADER_PATTERN;
	
	// Attempted to verify that each spectra record was a properly formed array of integers
	// using the following regular expression: "\\s*((\\d+\\s*,\\s*)*\\d+)\\s*[,]?\\s*".
	// However, this caused a stack overflow exception on records with ~5000 characters,
	// so a simplified regular expression is used to check if the record is approximately correct.
	private static final Pattern AMF_SPECTRA_FILE_RECORD_PATTERN = Pattern.compile("([\\d\\s,]+)");

	private static final String AMF_SPECTRA_SPLIT_RECORD_REGEX = "\\s*,\\s*";
	
	public static final int DEFAULT_SED_NCHANNELS = 2048;
	public static final int DEFAULT_FED_NCHANNELS = 2048;
	
	private static final double MAXIMUM_STEP_ERROR = 0.001;

	/// Parameters to be configured by a ConverterFactory ///
	private File amfDataFile = null;
	private File amfSpectraFile = null;
	
	private String[] elements = { };
	
	private int posXSetpointIdx = -1;
	private int posYSetpointIdx = -1;
	
	private int posXFeedbackIdx = -1;
	private int posYFeedbackIdx = -1;
	
	// Parameters for Beam Current. //
	private int srCurrentIdx = -1;
	private int[] mcsCurrentIdx = { -1, -1, -1, -1, -1, -1 };
	
	// Parameters for the Single Element Detector. //
	private int sedFastCountIdx = -1;
	private int sedSlowCountIdx = -1;
	private int sedDeadTimePctIdx = -1;
	private int sedElapsedRealTimeIdx = -1;
	private int sedElpasedLiveTimeIdx = -1;
	private boolean singleElement = false;
	
	private int sedDefaultNChannels = DEFAULT_SED_NCHANNELS;
	
	// Parameters for the Four Element Detector. //
	private int[] fedFastCountIdx = { -1, -1, -1, -1 };
	private int[] fedSlowCountIdx = { -1, -1, -1, -1 };
	private int[] fedDeadTimePctIdx = { -1, -1, -1, -1 };
	private int[] fedElapsedRealTimeIdx = { -1, -1, -1, -1 };
	private int[] fedElpasedLiveTimeIdx = { -1, -1, -1, -1 };
	private boolean fourElement = false;

	private int fedDefaultNChannels = DEFAULT_FED_NCHANNELS;
	//////////////////////////////////////////////////////////
	
	private int dataRecordIndex = -1;
	
	private BufferedReader amfDataFileReader;
	private BufferedReader amfSpectraFileReader;
	
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
	
	public MapXYVespersFromAMFConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}

	@Override
	public ConverterMap convert() throws ConverterException {
	
		if(!hasPositionXY()) {
			throw new ConverterException("Data file does not contain required X and Y positions.");
		}

		try {
			amfDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(amfDataFile)));
		}
		catch(FileNotFoundException e) {
			throw new ConverterException("The required AM data file could not be openned: " + e.getMessage());
		}
		
		if(hasSingleElementDetector() || hasFourElementDetector()) {
			try {
				amfSpectraFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(amfSpectraFile)));
			}
			catch(FileNotFoundException e) {
				throw new ConverterException("The required AM spectra file could not be openned: " + e.getMessage());
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
			else if((amfDataFile != null) && (amfDataFile.lastModified() > epoch.getTime())) {
				startTime = new Date(amfDataFile.lastModified());
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
			
			Matcher m;
			String line;
			
			while(true) {
				
				try {
					line = amfDataFileReader.readLine();
				}
				catch(IOException e) {
					throw new ConverterException("Error reading data file at record index: " + dataRecordIndex + ". " + e.getMessage());
				}
				
				if(line == null) {
					// end of data file found //
					break;
				}
				
				if(AMF_DATA_FILE_BLANKLINE_PATTERN.matcher(line).matches()) {
					// ignore lines containing only whitespace //
					continue;
				}
				
				m = AMF_DATA_FILE_HEADER_PATTERN.matcher(line);
				if(m.matches()) {
					// skip data file header section //
					continue;
				}
				
				m = AMF_DATA_FILE_RECORD_PATTERN.matcher(line);
				if(m.matches()) {
					dataRecordIndex++;
					processDataRecord(m.group(1).split(AMF_DATA_SPLIT_RECORD_REGEX));
				}
				else {
					throw new ConverterException("Error parsing data record at index: " + dataRecordIndex + ". No match to specified patern.");
				}
			}
			
			getAdapter().closeDestination();
		}
		catch(ConverterException e) {
			getAdapter().deleteDestination();
			throw e;
		}
		
		return getResponse();
	}
		
	private void processDataRecord(String[] dataRecord) throws ConverterException {
		
		if(dataRecordIndex == 0) {
			
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
					name = elements[getPosXFeedbackIdx()];
					desc = "Position X Feedback";
				}
				else {
					name = elements[getPosXSetpointIdx()];
					desc = "Position X Setpoint";
				}
				getAdapter().initPositionX(name, desc);

				if(getPosYFeedbackIdx() >= 0) {
					name = elements[getPosYFeedbackIdx()];
					desc = "Position Y Feedback";
				}
				else {
					name = elements[getPosYSetpointIdx()];
					desc = "Position Y Setpoint";
				}
				getAdapter().initPositionY(name, desc);
			}
			
			if(hasBeamCurrent()) {
				if(getSrCurrentIdx() >= 0) {
					name = elements[getSrCurrentIdx()];
					desc = "Storage Ring Current";
					getAdapter().initSrCurrent(name, desc);
				}
				
				for(int i=0; i<getMcsCurrentIdx().length; i++) {
					if(getMcsCurrentIdx()[i] >= 0) {
						name = elements[getMcsCurrentIdx()[i]];
						desc = "MCS Current";
						getAdapter().initMcsCurrent(i, name, desc);
					}
				}
			}

			if(hasSingleElementDetector()) {
						
				if(getSedFastCountIdx() >= 0) {
					name = elements[getSedFastCountIdx()];
					desc = "Single Element Fast Count";
					getAdapter().initSedFastCount(name, desc);
				}

				if(getSedSlowCountIdx() >= 0) {
					name = elements[getSedSlowCountIdx()];
					desc = "Single Element Slow Count";
					getAdapter().initSedSlowCount(name, desc);
				}
				
				if(getSedDeadTimePctIdx() >= 0) {
					name = elements[getSedDeadTimePctIdx()];
					desc = "Single Element Deadtime Percent";
					getAdapter().initSedDeadTimePct(name, desc);
				}
				
				if(getSedElapsedRealTimeIdx() >= 0) {
					name = elements[getSedElapsedRealTimeIdx()];
					desc = "Single Element Elapsed Real Time";
					getAdapter().initSedElapsedRealTime(name, desc);
				}
				
				if(getSedElpasedLiveTimeIdx() >= 0) {
					name = elements[getSedElpasedLiveTimeIdx()];
					desc = "Single Element Elapsed Live Time";
					getAdapter().initSedElapsedLiveTime(name, desc);
				}
			
				if(sedNChannels < 0) {
					sedNChannels = getSedDefaultNChannels();
				}
				
				name = "spectrum";
				desc = "Single Element Spectrum";
				getAdapter().initSedSpectrum(sedNChannels, name, desc);
				
			}
			else if(hasFourElementDetector()) {
					
				if(allGreaterThanOrEqual(getFedFastCountIdx(), 0)) {
					for(int i=0; i<getFedFastCountIdx().length; i++) {
						name = elements[getFedFastCountIdx()[i]];
						desc = "Four Element Fast Count (" + i + ")";
						getAdapter().initFedFastCount(i, name, desc);
					}
				}
			
				if(allGreaterThanOrEqual(getFedSlowCountIdx(), 0)) {
					for(int i=0; i<getFedSlowCountIdx().length; i++) {
						name = elements[getFedSlowCountIdx()[i]];
						desc = "Four Element Slow Count (" + i  + ")";
						getAdapter().initFedSlowCount(i, name, desc);
					}
				}
				
				if(allGreaterThanOrEqual(getFedDeadTimePctIdx(), 0)) {
					for(int i=0; i<getFedDeadTimePctIdx().length; i++) {
						name = elements[getFedDeadTimePctIdx()[i]];
						desc = "Four Element Deadtime Percent (" + i + ")";
						getAdapter().initFedDeadTimePct(i, name, desc);
					}
				}
					
				if(allGreaterThanOrEqual(getFedElapsedRealTimeIdx(), 0)) {
					for(int i=0; i<getFedElapsedRealTimeIdx().length; i++) {
						name = elements[getFedElapsedRealTimeIdx()[i]];
						desc = "Four Element Elapsed Real Time (" + i + ")";
						getAdapter().initFedElapsedRealTime(i, name, desc);
					}
				}
				
				if(allGreaterThanOrEqual(getFedElpasedLiveTimeIdx(), 0)) {
					for(int i=0; i<getFedElpasedLiveTimeIdx().length; i++) {
						name = elements[getFedElpasedLiveTimeIdx()[i]];
						desc = "Four Element Elapsed Live Time (" + i + ")";
						getAdapter().initFedElapsedLiveTime(i, name, desc);
					}
				}
							
				if(fedNChannels < 0) {
					fedNChannels = getFedDefaultNChannels();
				}
			

				name = "sumspectrum";
				desc = "Four Element Sum Spectrum";
				getAdapter().initFedSumSpectrum(fedNChannels, name, desc);

				for(int i=0; i<4; i++) {
					name = "spectrum" + i;
					desc = "Four Element Spectrum (" + i + ")";
					getAdapter().initFedSpectrum(i, fedNChannels, name, desc);
				}
			}
		}
		
		getAdapter().openDataRecord(dataRecordIndex);
		
		try {
			if(dataRecordIndex == 0) {
				prevX = Double.valueOf(dataRecord[getPosXSetpointIdx()]);
				prevY = Double.valueOf(dataRecord[getPosYSetpointIdx()]);
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
				nextX = Double.valueOf(dataRecord[getPosXSetpointIdx()]);
				nextY = Double.valueOf(dataRecord[getPosYSetpointIdx()]);
		
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
		catch(NumberFormatException e) {
			throw new ConverterException("Number format error getting setpoint from data record with index: " + dataRecordIndex, e);
		}
		
		// MapXY11 Category //

		getAdapter().recPointX(pointX);
		getAdapter().recPointY(pointY);
		
		try {
			posX = Double.valueOf(dataRecord[getPosXFeedbackIdx()]);
		}
		catch (NumberFormatException e) {
			posX = nextX; // Fall-back to setpointX //
		}
		getAdapter().recPositionX(posX);

		try {
			posY = Double.valueOf(dataRecord[getPosYFeedbackIdx()]);
		}
		catch (NumberFormatException e) {
			posY = nextY; // Fall-back to setpointY //
		}
		getAdapter().recPositionY(posY);

		
		// BeamI11 Category //
		
		if(hasBeamCurrent()) {
			try {
				if(getSrCurrentIdx() >= 0) {
					getAdapter().recSrCurrent(Double.valueOf(dataRecord[getSrCurrentIdx()]));
				}

				for(int i=0; i<getMcsCurrentIdx().length; i++) {
					if(getMcsCurrentIdx()[i] >= 0) {
						getAdapter().recMcsCurrent(i, Double.valueOf(dataRecord[getMcsCurrentIdx()[i]]));
					}
				}
			}
			catch(NumberFormatException e) {
				throw new ConverterException("Number format error getting beam current from data record with index: " + dataRecordIndex);
			}
		}
		
		// MCA10 Category //
		
		if(hasSingleElementDetector() || hasFourElementDetector()) {
		
			Matcher m;
			String line;
			String[] spectrumRecord;
			
			while(true) {
				
				try {
					line = amfSpectraFileReader.readLine();
				}
				catch(IOException e) {
					throw new ConverterException("Error reading spectra file at index: " + dataRecordIndex + ". " + e.getMessage());
				}
				
				if(line == null) {
					throw new ConverterException("Unexpected end-of-file while reading spectra file at data index: " + dataRecordIndex);
				}
				
				if(AMF_SPECTRA_FILE_BLANKLINE_PATTERN.matcher(line).matches()) {
					// ignore lines containing only whitespace //
					continue;
				}
				
				m = AMF_SPECTRA_FILE_HEADER_PATTERN.matcher(line);
				if(m.matches()) {
					// skip spectra file comments //
					continue;
				}
				
				m = AMF_SPECTRA_FILE_RECORD_PATTERN.matcher(line);
				if(m.matches()) {
					spectrumRecord = m.group(1).split(AMF_SPECTRA_SPLIT_RECORD_REGEX);
					break;
				}
				else {
					throw new ConverterException("Error parsing spectra record at index: " + dataRecordIndex + ". No match to specified patern.");
				}
			}
		
			if(hasSingleElementDetector()) {
				
				if(spectrumRecord.length < sedNChannels) {
					throw new ConverterException("Single element spectrum contains only " + spectrumRecord.length + " channels.");
				}
				
				try {
					if(getSedFastCountIdx() >= 0) {
						getAdapter().recSedFastCount(Double.valueOf(dataRecord[getSedFastCountIdx()]).longValue());
					}
					
					if(getSedSlowCountIdx() >= 0) {
						getAdapter().recSedSlowCount(Double.valueOf(dataRecord[getSedSlowCountIdx()]).longValue());
					}
					
					if(getSedDeadTimePctIdx() >= 0) {
						getAdapter().recSedDeadTimePct(Double.valueOf(dataRecord[getSedDeadTimePctIdx()]));
					}
				
					if(getSedElapsedRealTimeIdx() >= 0) {
						getAdapter().recSedElapsedRealTime(Double.valueOf(dataRecord[getSedElapsedRealTimeIdx()]));
					}
				
					if(getSedElpasedLiveTimeIdx() >= 0) {
						getAdapter().recSedElapsedLiveTime(Double.valueOf(dataRecord[getSedElpasedLiveTimeIdx()]));
					}
										
					getAdapter().recSedSpectrum(toLongArray(spectrumRecord, 0, sedNChannels));
				}
				catch(NumberFormatException e) {
					throw new ConverterException("Number format error while getting SED data from record at index: " + dataRecordIndex + ". " + e.getMessage());
				}
			}
			else if(hasFourElementDetector()) {
				
				if(spectrumRecord.length < (fedNChannels * 4)) {
					throw new ConverterException("Four element spectrum contains only " + spectrumRecord.length + " channels.");
				}
				
				boolean hasSumSpectrum = true;
				if(spectrumRecord.length < (fedNChannels * 5)) {
					hasSumSpectrum = false;
				}
				
				try {
					if(allGreaterThanOrEqual(fedFastCountIdx, 0)) {
						for(int i=0; i<fedFastCountIdx.length; i++) {
							getAdapter().recFedFastCount(i, Double.valueOf(dataRecord[getFedFastCountIdx()[i]]).longValue());
						}
					}
					
					if(allGreaterThanOrEqual(fedSlowCountIdx, 0)) {
						for(int i=0; i<fedSlowCountIdx.length; i++) {
							getAdapter().recFedSlowCount(i, Double.valueOf(dataRecord[getFedSlowCountIdx()[i]]).longValue());
						}
					}
				
					if(allGreaterThanOrEqual(fedDeadTimePctIdx, 0)) {
						for(int i=0; i<fedDeadTimePctIdx.length; i++) {
							getAdapter().recFedDeadTimePct(i, Double.valueOf(dataRecord[getFedDeadTimePctIdx()[i]]));
						}
					}
					
					if(allGreaterThanOrEqual(fedElapsedRealTimeIdx, 0)) {
						for(int i=0; i<fedElapsedRealTimeIdx.length; i++) {
							getAdapter().recFedElapsedRealTime(i, Double.valueOf(dataRecord[getFedElapsedRealTimeIdx()[i]]));
						}
					}
				
					if(allGreaterThanOrEqual(fedElpasedLiveTimeIdx, 0)) {
						for(int i=0; i<fedElpasedLiveTimeIdx.length; i++) {
							getAdapter().recFedElapsedLiveTime(i, Double.valueOf(dataRecord[getFedElpasedLiveTimeIdx()[i]]));
						}
					}
				
					if(hasSumSpectrum) {
						getAdapter().recFedSumSpectrum(toLongArray(spectrumRecord, (fedNChannels * 4), fedNChannels));
					}
					
					for(int i=0; i<4; i++) {
						getAdapter().recFedSpectrum(i, toLongArray(spectrumRecord, (fedNChannels * i), fedNChannels));
					}
				}
				catch(NumberFormatException e) {
					throw new ConverterException("Number format error while getting FED data from record at index: " + dataRecordIndex + ". " + e.getMessage());
				}
			}
		}
		
		getAdapter().closeDataRecord(dataRecordIndex);
	}
	
	protected long[] toLongArray(String[] sarray, int start, int length) throws NumberFormatException {
		int end = start + length;
		long[] larray = new long[length];
		for(int idx=start; idx<end; idx++) {
			try {
				// Parsing an integer is fastest, so try it first. //
				larray[idx] = Long.valueOf(sarray[idx]);
			}
			catch(NumberFormatException e) {
				// Parse as floating point and convert to integer. //
				larray[idx] = Double.valueOf(sarray[idx]).longValue();
			}
		}
		return larray;
	}
	
	protected boolean hasPositionXY() {
		return (amfDataFile != null) &&
					(posXFeedbackIdx >= 0) && (posYFeedbackIdx >= 0);
	}

	protected boolean hasBeamCurrent() {
		return (amfDataFile != null) &&
					((srCurrentIdx >= 0) || oneGreaterThanOrEqual(mcsCurrentIdx, 0));
	}
	
	protected boolean hasSingleElementDetector() {
		return (amfSpectraFile != null) && isSingleElement();
	}

	protected boolean hasFourElementDetector() {
		return (amfSpectraFile != null) && isFourElement();
	}
	
	public File getAmfDataFile() {
		return amfDataFile;
	}
	public void setAmfDataFile(File amfDataFile) {
		this.amfDataFile = amfDataFile;
	}

	public File getAmfSpectraFile() {
		return amfSpectraFile;
	}
	public void setAmfSpectraFile(File amfSpectraFile) {
		this.amfSpectraFile = amfSpectraFile;
	}

	public String[] getElements() {
		return elements;
	}
	public void setElements(String[] elements) {
		this.elements = elements;
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

	public boolean isSingleElement() {
		return singleElement;
	}
	public void setSingleElement(boolean singleElement) {
		this.singleElement = singleElement;
	}

	public int getSedDefaultNChannels() {
		return sedDefaultNChannels;
	}
	public void setSedDefaultNChannels(int sedDefaultNChannels) {
		this.sedDefaultNChannels = sedDefaultNChannels;
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
	
	public boolean isFourElement() {
		return fourElement;
	}
	public void setFourElement(boolean fourElement) {
		this.fourElement = fourElement;
	}

	public int getFedDefaultNChannels() {
		return fedDefaultNChannels;
	}
	public void setFedDefaultNChannels(int fedDefaultNChannels) {
		this.fedDefaultNChannels = fedDefaultNChannels;
	}
}
