/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapVespersConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private static final String DAF_DATA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_DAF_DATA));
	private static final String DAF_SPECTRA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_DAF_SPEC));
	
	private static final Pattern DAF_DATA_FILE_NAME_PATTERN = Pattern.compile(DAF_DATA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	private static final Pattern DAF_SPECTRA_FILE_NAME_PATTERN = Pattern.compile(DAF_SPECTRA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	
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
		
		// DAF Data File //
		Object dafDataFile = request.get(REQUEST_KEY_DAF_DATA_FILE);
		if(!(dafDataFile instanceof File)) {
			if(isNotEmptyString(scanParams.get(PARAM_KEY_DAF_FILE_NAME))) {
				String dafDataFileName = scanParams.get(PARAM_KEY_DAF_FILE_NAME);
				dafDataFile = new File(scanDataUrl, dafDataFileName);
				request.put(REQUEST_KEY_DAF_DATA_FILE, dafDataFile);
			}
			else if(isNotEmptyString(scanParams.get(PARAM_KEY_DATA_FILE_BASE))) {
				String dataFileBase = scanParams.get(PARAM_KEY_DATA_FILE_BASE);
				dafDataFile = new File(scanDataUrl, dataFileBase + FILE_NAME_SUFFIX_DAF_DATA);
				request.put(REQUEST_KEY_DAF_DATA_FILE, dafDataFile);
			}
			else {
				throw new ConverterFactoryException("Scan parameters do not contain a DAF file name.");
			}
		}
		
		Matcher dafDataFileNameMatcher; 
		if(dafDataFile instanceof File) {
			dafDataFileNameMatcher = DAF_DATA_FILE_NAME_PATTERN.matcher(((File)dafDataFile).getName());
			if(!dafDataFileNameMatcher.matches()) {
				throw new ConverterFactoryException("The required DAF data file does not have a '" + FILE_NAME_SUFFIX_DAF_DATA + "' extension.");
			}
		}
		else {
			throw new ConverterFactoryException("The required DAF data file not found in converter request.");
		}
		
		// DAF Spectra File //
		Object dafSpectraFile = request.get(REQUEST_KEY_DAF_SPEC_FILE);
		if(!(dafSpectraFile instanceof File)) {
			String dafSpectraFileName = dafDataFileNameMatcher.group(1) + FILE_NAME_SUFFIX_DAF_SPEC;
			dafSpectraFile = new File(scanDataUrl, dafSpectraFileName);
			request.put(REQUEST_KEY_DAF_SPEC_FILE, dafSpectraFile);
		}
		
		Matcher dafSpectraFileNameMatcher;
		if(dafSpectraFile instanceof File) {
			dafSpectraFileNameMatcher = DAF_SPECTRA_FILE_NAME_PATTERN.matcher(((File)dafSpectraFile).getName());
			if(!dafSpectraFileNameMatcher.matches()) {
				throw new ConverterFactoryException("The required DAF spectra file does not have a '" + FILE_NAME_SUFFIX_DAF_SPEC + "' extension.");
			}
		}
		else {
			throw new ConverterFactoryException("The required DAF spectra file not found in converter request.");
		}
		
		return request;
	}
}
