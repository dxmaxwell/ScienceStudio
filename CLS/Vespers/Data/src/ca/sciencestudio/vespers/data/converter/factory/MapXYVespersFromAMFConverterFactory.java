/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersFromAMFConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.util.Parameters;
import ca.sciencestudio.data.util.ElementOptions;
import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.vespers.data.converter.MapXYVespersFromAMFConverter;

/**
 * @author maxweld
 * 
 *
 */
public class MapXYVespersFromAMFConverterFactory extends AbstractMapXYVespersConverterFactory {
	
	private static final String[] SUPPORTED_FROM_FORMATS = { ConverterMap.DEFAULT_FROM_FORMAT, "AMF" };
	
	private static final Pattern AMF_DATA_FILE_BLANKLINE_PATTERN = Pattern.compile("\\s*");
	
	private static final Pattern AMF_DATA_FILE_HEADER_PATTERN = Pattern.compile("^\\s*#\\s*(.*?)\\s*$");
	
	private static final Pattern AMF_DATA_FILE_SCAN_PATTERN = Pattern.compile("Scan:\\s*(.*)");
	private static final Pattern AMF_DATA_FILE_DATE_PATTERN = Pattern.compile("Date:\\s*(\\d{4} \\d{2} \\d{2} \\d{2}:\\d{2}:\\d{2})");
	private static final Pattern AMF_DATA_FILE_SAMPLE_PATTERN = Pattern.compile("Sample:\\s*(.*)");
	private static final Pattern AMF_DATA_FILE_FACILITY_PATTERN = Pattern.compile("Facility:\\s*(.*)");
	
	private static final Pattern AMF_DATA_FILE_INAUGHT_PATTERN = Pattern.compile("I0:\\s*(\\S+)\\s*-\\s*(.*)");
	private static final Pattern AMF_DATA_FILE_XRF_DETECTOR_PATTERN = Pattern.compile("Fluorescence Detector:\\s*(.*)");
	
	private static final Pattern AMF_DATA_FILE_ELEMLINE_PATTERN = Pattern.compile("[\\-]+");
	private static final Pattern AMF_DATA_FILE_ELEMENTS_PATTERN = Pattern.compile("((\\S+\\s+)*\\S+)\\s*");
	
	private static final DateFormat AMF_DATA_START_DATE_FORMAT = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
	
	private static final String AMF_DATA_SPLIT_ELEMENTS_REGEX = "\\s+";
	
	private static final String AMF_DATA_SINGLE_ELEMENT_DETECTOR = "Single Element Vortex Detector";
	private static final String AMF_DATA_FOUR_ELEMENT_DETECTOR = "Four Element Vortex Detector";
	
	// Parameters for the sample position. //
	private ElementOptions posXSetpointOptions;
	private ElementOptions posYSetpointOptions;
	
	private ElementOptions posXFeedbackOptions;
	private ElementOptions posYFeedbackOptions;
	
	// Beam current (election or x-ray). //
	private ElementOptions srCurrentOptions;
	private List<ElementOptions> mcsCurrentOptions;
	
	// Parameters for the Single Element Detector. //
	private ElementOptions sedFastCountOptions;
	private ElementOptions sedSlowCountOptions;
	private ElementOptions sedDeadTimePctOptions;
	private ElementOptions sedElapsedRealTimeOptions;
	private ElementOptions sedElpasedLiveTimeOptions;
	
	private int sedDefaultNChannels = MapXYVespersFromAMFConverter.DEFAULT_SED_NCHANNELS;
	
	// Parameters for the Four Element Detector. //
	private List<ElementOptions> fedFastCountOptions;
	private List<ElementOptions> fedSlowCountOptions;
	private List<ElementOptions> fedDeadTimePctOptions;
	private List<ElementOptions> fedElapsedRealTimeOptions;
	private List<ElementOptions> fedElpasedLiveTimeOptions;
	
	private int fedDefaultNChannels = MapXYVespersFromAMFConverter.DEFAULT_FED_NCHANNELS;
	
	@Override
	public Converter getConverter(ConverterMap request) throws ConverterFactoryException {
		
		request = validateRequest(request);
		
		boolean forceUpdate = request.isForceUpdate();
		String fromFormat = request.getFromFormat();
		String toFormat = request.getToFormat();
		
		
		File amfDataFile = (File)request.get(REQUEST_KEY_DATA_FILE);
		if(!amfDataFile.exists()) {
			throw new ConverterFactoryException("The required AM data file not found here: " + amfDataFile);
		}
		
		File amfSpectraFile = (File)request.get(REQUEST_KEY_SPECTRA_FILE);
		if(!amfSpectraFile.exists()) {
			throw new ConverterFactoryException("The required AM spectra file not found here: " + amfSpectraFile);
		}
		
		// Read Data File Header //
		
		BufferedReader amfDataFileReader;
		try {
			amfDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(amfDataFile)));
		}
		catch(FileNotFoundException e) {
			throw new ConverterFactoryException("The required AM data file could not be openned: " + e.getMessage());
		}
		
		Matcher m;
		
		boolean scanNext = true;
		boolean elementsNext = false;
		
		String inaught = null;
		String xrfDetector = null;
		
		String[] elements = null;
		
		String line;
		
		while(true) {
			
			try {
				line = amfDataFileReader.readLine();
			}
			catch(IOException e) {
				break;
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
			if(!m.matches()) {
				// end of data file header section //
				break;
			}
			
			line = m.group(1);
			
			if(scanNext) {
				m = AMF_DATA_FILE_SCAN_PATTERN.matcher(line);
				if(m.matches()) {
					if(DEFAULT_SCAN_NAME.equals(request.get(REQUEST_KEY_SCAN_NAME))) {
						request.put(REQUEST_KEY_SCAN_NAME, m.group(1));
					}
					scanNext = false;
					continue;
				}
				
				throw new ConverterFactoryException("Unexpected first line in AM data file.");
			}
				
			if(elementsNext) {
				m = AMF_DATA_FILE_ELEMENTS_PATTERN.matcher(line);
				if(m.matches()) {
					elements = m.group(1).split(AMF_DATA_SPLIT_ELEMENTS_REGEX);
					elementsNext = false;
					continue;
				}
				
				throw new ConverterFactoryException("Unexpected line following '# ----' in AM data file.");
			}
			
			m = AMF_DATA_FILE_DATE_PATTERN.matcher(line);
			if(m.matches()) {
				if(DEFAULT_SCAN_START_DATE.equals(request.get(REQUEST_KEY_SCAN_START_DATE))) {
					try {
						Date startDate = AMF_DATA_START_DATE_FORMAT.parse(m.group(1));
						request.put(REQUEST_KEY_SCAN_START_DATE, startDate);
						request.put(REQUEST_KEY_SCAN_END_DATE, startDate);
					}
					catch(ParseException e) {
						// ignore exception //
					}
				}
				continue;
			}
			
			m = AMF_DATA_FILE_SAMPLE_PATTERN.matcher(line);
			if(m.matches()) {
				if(DEFAULT_SAMPLE_NAME.equals(request.get(REQUEST_KEY_SAMPLE_NAME))) {
					request.put(REQUEST_KEY_SAMPLE_NAME,  m.group(1));
				}
				continue;
			}
			
			m = AMF_DATA_FILE_FACILITY_PATTERN.matcher(line);
			if(m.matches()) {
				if(DEFAULT_FACILITY_NAME.equals(request.get(REQUEST_KEY_FACILITY_NAME))) {
					request.put(REQUEST_KEY_LABORATORY_LONG_NAME, m.group(1));
				}
				continue;
			}
			
			m = AMF_DATA_FILE_XRF_DETECTOR_PATTERN.matcher(line);
			if(m.matches()) {
				xrfDetector = m.group(1);
				continue;
			}
			
			m = AMF_DATA_FILE_INAUGHT_PATTERN.matcher(line);
			if(m.matches()) {
				inaught = m.group(1);
				continue;
			}
			
			m = AMF_DATA_FILE_ELEMLINE_PATTERN.matcher(line);
			if(m.matches()) {
				elementsNext = true;
				continue;
			}
		}
		
		int posXSetpointIdx = posXSetpointOptions.getElementIndex(elements);
		int posYSetpointIdx = posYSetpointOptions.getElementIndex(elements);
		
		if((posXSetpointIdx < 0 || posYSetpointIdx < 0)) {
			throw new ConverterFactoryException("No element found that contains the MapXY position coordinates.");
		}
		
		MapXYVespersFromAMFConverter converter = new MapXYVespersFromAMFConverter(fromFormat, toFormat, forceUpdate);
		
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
		
		converter.setAmfDataFile(amfDataFile);
		converter.setAmfSpectraFile(amfSpectraFile);
		
		converter.setElements(elements);
		
		converter.setPosXSetpointIdx(posXSetpointIdx);
		converter.setPosYSetpointIdx(posYSetpointIdx);
		converter.setPosXFeedbackIdx(posXFeedbackOptions.getElementIndex(elements));
		converter.setPosYFeedbackIdx(posYFeedbackOptions.getElementIndex(elements));
		
		prepareBeamCurrent(converter, elements, inaught);
		
		if(prepareSingleElementDetector(converter, elements, xrfDetector)) {
			prepareAdapter(converter, request);
			return converter;
		}
		
		if(prepareFourElementDetector(converter, elements, xrfDetector)) {
			prepareAdapter(converter, request);
			return converter;
		}
			
		throw new ConverterFactoryException("No MultiChannel Analyzer spectrum found in DAF data file.");
	}
	
	protected void prepareBeamCurrent(MapXYVespersFromAMFConverter converter, String[] elements, String inaught) {
		converter.setSrCurrentIdx(srCurrentOptions.getElementIndex(elements));
		
		List<Integer> mcsCurrentIdxList = new ArrayList<Integer>();
		for(ElementOptions elementOption : mcsCurrentOptions) {
			int idx = elementOption.getElementIndex(elements);
			if(idx >= 0) {
				if(elements[idx].equals(inaught)) {
					mcsCurrentIdxList.add(0, idx);
				} else {
					mcsCurrentIdxList.add(idx);
				}
			}
		}

		int[] mcsCurrentIdx = new int[mcsCurrentIdxList.size()];
		for(int idx=0; idx<mcsCurrentIdx.length; idx++) {
			mcsCurrentIdx[idx] = mcsCurrentIdxList.get(idx);
		}
		converter.setMcsCurrentIdx(mcsCurrentIdx);
	}
	
	protected boolean prepareSingleElementDetector(MapXYVespersFromAMFConverter converter, String[] elements, String xrfDetector) {
		
		if(!AMF_DATA_SINGLE_ELEMENT_DETECTOR.equals(xrfDetector)) {
			return false;
		}
			
		int sedFastCountIdx = sedFastCountOptions.getElementIndex(elements);
		int sedSlowCountIdx = sedSlowCountOptions.getElementIndex(elements);
		int sedDeadTimePctIdx = sedDeadTimePctOptions.getElementIndex(elements);
		int sedElapsedRealTimeIdx = sedElapsedRealTimeOptions.getElementIndex(elements);
		int sedElpasedLiveTimeIdx = sedElpasedLiveTimeOptions.getElementIndex(elements);
		
		converter.setSingleElement(true);
		converter.setSedFastCountIdx(sedFastCountIdx);
		converter.setSedSlowCountIdx(sedSlowCountIdx);
		converter.setSedDeadTimePctIdx(sedDeadTimePctIdx);
		converter.setSedElapsedRealTimeIdx(sedElapsedRealTimeIdx);
		converter.setSedElpasedLiveTimeIdx(sedElpasedLiveTimeIdx);
		
		//converter.setSedDefaultMaxEnergy(sedDefaultMaxEnergy);
		converter.setSedDefaultNChannels(sedDefaultNChannels);
		return true;
	}
	
	
	protected boolean prepareFourElementDetector(MapXYVespersFromAMFConverter converter, String[] elements, String xrfDetector) {
		
		if(!AMF_DATA_FOUR_ELEMENT_DETECTOR.equals(xrfDetector)) {
			return false;
		}
		
		int nElements = 4;
		int[] fedFastCountIdx = new int[nElements];
		int[] fedSlowCountIdx = new int[nElements];
		int[] fedDeadTimePctIdx = new int[nElements];
		int[] fedElapsedRealTimeIdx = new int[nElements];
		int[] fedElpasedLiveTimeIdx = new int[nElements];
		for(int idx=0; idx<nElements; idx++) {
			fedFastCountIdx[idx] = fedFastCountOptions.get(idx).getElementIndex(elements);
			fedSlowCountIdx[idx] = fedSlowCountOptions.get(idx).getElementIndex(elements);
			fedDeadTimePctIdx[idx] = fedDeadTimePctOptions.get(idx).getElementIndex(elements);
			fedElapsedRealTimeIdx[idx] = fedElapsedRealTimeOptions.get(idx).getElementIndex(elements);
			fedElpasedLiveTimeIdx[idx] = fedElpasedLiveTimeOptions.get(idx).getElementIndex(elements);
		}
		
		converter.setFourElement(true);
		converter.setFedFastCountIdx(fedFastCountIdx);
		converter.setFedSlowCountIdx(fedSlowCountIdx);
		converter.setFedDeadTimePctIdx(fedDeadTimePctIdx);
		converter.setFedElapsedRealTimeIdx(fedElapsedRealTimeIdx);
		converter.setFedElpasedLiveTimeIdx(fedElpasedLiveTimeIdx);
		
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
		
		return super.validateRequest(request);
	}

	public void setPosXSetpointOptions(ElementOptions posXSetpointOptions) {
		this.posXSetpointOptions = posXSetpointOptions;
	}

	public void setPosYSetpointOptions(ElementOptions posYSetpointOptions) {
		this.posYSetpointOptions = posYSetpointOptions;
	}

	public void setPosXFeedbackOptions(ElementOptions posXFeedbackOptions) {
		this.posXFeedbackOptions = posXFeedbackOptions;
	}

	public void setPosYFeedbackOptions(ElementOptions posYFeedbackOptions) {
		this.posYFeedbackOptions = posYFeedbackOptions;
	}

	public void setSrCurrentOptions(ElementOptions srCurrentOptions) {
		this.srCurrentOptions = srCurrentOptions;
	}

	public void setMcsCurrentOptions(List<ElementOptions> mcsCurrentOptions) {
		this.mcsCurrentOptions = mcsCurrentOptions;
	}

	public void setSedFastCountOptions(ElementOptions sedFastCountOptions) {
		this.sedFastCountOptions = sedFastCountOptions;
	}

	public void setSedSlowCountOptions(ElementOptions sedSlowCountOptions) {
		this.sedSlowCountOptions = sedSlowCountOptions;
	}

	public void setSedDeadTimePctOptions(ElementOptions sedDeadTimePctOptions) {
		this.sedDeadTimePctOptions = sedDeadTimePctOptions;
	}

	public void setSedElapsedRealTimeOptions(ElementOptions sedElapsedRealTimeOptions) {
		this.sedElapsedRealTimeOptions = sedElapsedRealTimeOptions;
	}

	public void setSedElpasedLiveTimeOptions(ElementOptions sedElpasedLiveTimeOptions) {
		this.sedElpasedLiveTimeOptions = sedElpasedLiveTimeOptions;
	}

	public void setSedDefaultNChannels(int sedDefaultNChannels) {
		this.sedDefaultNChannels = sedDefaultNChannels;
	}

	public void setFedFastCountOptions(List<ElementOptions> fedFastCountOptions) {
		this.fedFastCountOptions = fedFastCountOptions;
	}

	public void setFedSlowCountOptions(List<ElementOptions> fedSlowCountOptions) {
		this.fedSlowCountOptions = fedSlowCountOptions;
	}

	public void setFedDeadTimePctOptions(List<ElementOptions> fedDeadTimePctOptions) {
		this.fedDeadTimePctOptions = fedDeadTimePctOptions;
	}

	public void setFedElapsedRealTimeOptions(List<ElementOptions> fedElapsedRealTimeOptions) {
		this.fedElapsedRealTimeOptions = fedElapsedRealTimeOptions;
	}

	public void setFedElpasedLiveTimeOptions(List<ElementOptions> fedElpasedLiveTimeOptions) {
		this.fedElpasedLiveTimeOptions = fedElpasedLiveTimeOptions;
	}

	public void setFedDefaultNChannels(int fedDefaultNChannels) {
		this.fedDefaultNChannels = fedDefaultNChannels;
	}
}
