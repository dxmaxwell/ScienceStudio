/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapVespersConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import java.io.File;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public abstract class AbstractMapVespersConverterFactory extends AbstractVespersConverterFactory implements StdScanParams {
	
	private static final String SUPPORTED_TECHNIQUE_NAME = "XRF";
	
	//private static final String DAF_DATA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_DAF_DATA));
	//private static final String DAF_SPECTRA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_DAF_SPEC));
	
	//private static final Pattern DAF_DATA_FILE_NAME_PATTERN = Pattern.compile(DAF_DATA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	//private static final Pattern DAF_SPECTRA_FILE_NAME_PATTERN = Pattern.compile(DAF_SPECTRA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	
	@Override
	@SuppressWarnings("deprecation")
	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {
		
		request = super.validateRequest(request);
		
		Object techniqueName = request.get(REQUEST_KEY_TECHNIQUE_NAME);
		if(!SUPPORTED_TECHNIQUE_NAME.equals(techniqueName)) {
			throw new ConverterFactoryException("Convert data for technique, " + techniqueName +", not supported.");
		}
		
		String scanDataUrl = (String)request.get(REQUEST_KEY_SCAN_DATA_URL);
		Parameters scanParams = (Parameters)request.get(REQUEST_KEY_SCAN_PARAMS);
		
		//   Do not know if I still need this??? //		
		//if(!"20090917A".equals(getDAFFileVersion())) {
		//	throw new ConvertRequestException("Data for DAF file version, " + getDAFFileVersion() + ", not supported.");
		//}
		
		// Input Data File //
		Object dataFile = request.get(REQUEST_KEY_DATA_FILE);
		if(!(dataFile instanceof File)) {
			if(isNotEmptyString(scanParams.get(PARAM_KEY_DAF_FILE_NAME))) {
				String dafDataFileName = scanParams.get(PARAM_KEY_DAF_FILE_NAME);
				dataFile = new File(scanDataUrl, dafDataFileName);
				request.put(REQUEST_KEY_DATA_FILE, dataFile);
			}
			else if(isNotEmptyString(scanParams.get(PARAM_KEY_DATA_FILE_BASE))) {
				String dataFileBase = scanParams.get(PARAM_KEY_DATA_FILE_BASE);
				dataFile = new File(scanDataUrl, dataFileBase + FILE_NAME_SUFFIX_DAF_DATA);
				request.put(REQUEST_KEY_DATA_FILE, dataFile);
			}
			else {
				throw new ConverterFactoryException("Scan parameters do not contain a data file base.");
			}
		}
		
		//  Check that data file extension is '.dat'. Is this needed? //
		//Matcher dafDataFileNameMatcher; 
		//if(dataFile instanceof File) {
		//	dafDataFileNameMatcher = DAF_DATA_FILE_NAME_PATTERN.matcher(((File)dataFile).getName());
		//	if(!dafDataFileNameMatcher.matches()) {
		//		throw new ConverterFactoryException("The required DAF data file does not have a '" + FILE_NAME_SUFFIX_DAF_DATA + "' extension.");
		//	}
		//}
		//else {
		//	throw new ConverterFactoryException("The required DAF data file not found in converter request.");
		//}
		
		// Input Spectra File //
		Object spectraFile = request.get(REQUEST_KEY_SPECTRA_FILE);
		if(!(spectraFile instanceof File)) {
			if(isNotEmptyString(scanParams.get(PARAM_KEY_DATA_FILE_BASE))) {
				String dataFileBase = scanParams.get(PARAM_KEY_DATA_FILE_BASE);
				spectraFile = new File(scanDataUrl, dataFileBase + FILE_NAME_SUFFIX_DAF_SPEC);
				request.put(REQUEST_KEY_SPECTRA_FILE, spectraFile);
			}
			else {
				throw new ConverterFactoryException("Scan parameters do not contain a data file base.");
			}
		}
		
		// Check that data file extension is '_spectra.dat'.  Is this needed? //
		//Matcher dafSpectraFileNameMatcher;
		//if(dafSpectraFile instanceof File) {
		//	dafSpectraFileNameMatcher = DAF_SPECTRA_FILE_NAME_PATTERN.matcher(((File)dafSpectraFile).getName());
		//	if(!dafSpectraFileNameMatcher.matches()) {
		//		throw new ConverterFactoryException("The required DAF spectra file does not have a '" + FILE_NAME_SUFFIX_DAF_SPEC + "' extension.");
		//	}
		//}
		//else {
		//	throw new ConverterFactoryException("The required DAF spectra file not found in converter request.");
		//}
		
		return request;
	}
}
