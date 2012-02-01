/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFtoCDFMLConverterFactory class.
 *     
 */
package ca.sciencestudio.data.converter.factory;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.converter.AbstractCDFtoCDFMLConverter;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.util.process.ProcessFactory;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class CDFtoCDFMLConverterFactory extends AbstractScanConverterFactory implements StdConverter, StdScanParams {

	public static final String REQUEST_KEY_CDF_DATA_FILE = CDFtoCDFMLConverterFactory.class.getName() + ".cdfDataFile";
	public static final String REQUEST_KEY_CDFML_DATA_FILE = CDFtoCDFMLConverterFactory.class.getName() + ".cdfmlDataFile";
	
	private static final String SUPPORTED_FROM_FORMAT = "CDF";
	private static final String SUPPORTED_TO_FORMAT = "CDFML";
	
	private static final String CDF_DATA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_CDF));
	private static final String CDFML_DATA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_CDFML));
	
	private static final Pattern CDF_DATA_FILE_NAME_PATTERN = Pattern.compile(CDF_DATA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	private static final Pattern CDFML_DATA_FILE_NAME_PATTERN = Pattern.compile(CDFML_DATA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	
	private ProcessFactory processFactory;
	private long processTimeout = CDFtoCDFMLConverter.DEFAULT_PROCESS_TIMEOUT;
	
	public Converter getConverter(ConverterMap request) throws ConverterFactoryException {
		
		request = validateRequest(request); 
		
		File cdfDataFile = (File) request.get(REQUEST_KEY_CDF_DATA_FILE);
		if(!cdfDataFile.exists()) {
			throw new ConverterFactoryException("The required CDF data file not found here: " + cdfDataFile);
		}
		
		File cdfmlDataFile = (File) request.get(REQUEST_KEY_CDFML_DATA_FILE);
		
		String toFormat = request.getToFormat();
		String fromFormat = request.getFromFormat();
		boolean forceUpdate = request.isForceUpdate();
		
		CDFtoCDFMLConverter converter = new CDFtoCDFMLConverter(fromFormat, toFormat, forceUpdate);
		converter.setCdfDataFile(cdfDataFile);
		converter.setCdfmlDataFile(cdfmlDataFile);
		converter.setProcessFactory(processFactory);
		converter.setProcessTimeout(processTimeout);
		
		return converter;
	}
	
	@SuppressWarnings("deprecation")
	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {
		
		request = super.validateRequest(request);
		
		if(!SUPPORTED_FROM_FORMAT.equals(request.getFromFormat())) {
			throw new ConverterFactoryException("Convert FROM format, " + request.getFromFormat() + ", not supported.");
		}
		
		if(!SUPPORTED_TO_FORMAT.equals(request.getToFormat())) {
			throw new ConverterFactoryException("Convert TO format, " + request.getToFormat() + ", not supported.");
		}
		
		String scanDataUrl = (String)request.get(REQUEST_KEY_SCAN_DATA_URL);
		Parameters scanParams = (Parameters) request.get(REQUEST_KEY_SCAN_PARAMS);
		
		// CDF Data File //
		Object cdfDataFile = request.get(REQUEST_KEY_CDF_DATA_FILE);
		if(!(cdfDataFile instanceof File)) {
			if(isNotEmptyString(scanParams.get(PARAM_KEY_DATA_FILE_BASE))) {
				String dataFileBase = scanParams.get(PARAM_KEY_DATA_FILE_BASE);
				cdfDataFile = new File(scanDataUrl, dataFileBase + FILE_NAME_SUFFIX_CDF);
				request.put(REQUEST_KEY_CDF_DATA_FILE, cdfDataFile);
			}
			else if(isNotEmptyString(scanParams.get(PARAM_KEY_CDF_FILE_NAME))) {
				String cdfDataFileName = scanParams.get(PARAM_KEY_CDF_FILE_NAME);
				cdfDataFile = new File(scanDataUrl, cdfDataFileName);
				request.put(REQUEST_KEY_CDF_DATA_FILE, cdfDataFile);
			}
			else if(isNotEmptyString(scanParams.get(PARAM_KEY_DAF_FILE_NAME))) {
				String dafDataFileName = scanParams.get(PARAM_KEY_DAF_FILE_NAME);
				String dafDataFileNameSuffixRegex = Pattern.quote(FILE_NAME_SUFFIX_DAF_DATA) + "\\Z";
				String cdfDataFileName = dafDataFileName.replaceAll(dafDataFileNameSuffixRegex, FILE_NAME_SUFFIX_CDF);
				cdfDataFile = new File(scanDataUrl, cdfDataFileName);
				request.put(REQUEST_KEY_CDF_DATA_FILE, cdfDataFile);
			}
			else {
				throw new ConverterFactoryException("Scan parameters do not contain a CDF file name.");
			}
		}
		
		Matcher cdfDataFileNameMatcher;
		if(cdfDataFile instanceof File) {
			cdfDataFileNameMatcher = CDF_DATA_FILE_NAME_PATTERN.matcher(((File)cdfDataFile).getName());
			if(!cdfDataFileNameMatcher.matches()) {
				throw new ConverterFactoryException("The required CDF data file does not have a '" + FILE_NAME_SUFFIX_CDF + "' extension.");
			}
		}
		else {
			throw new ConverterFactoryException("The required CDF data file not found in converter request.");
		}
		
		// CDFML Data File //
		Object cdfmlDataFile = request.get(REQUEST_KEY_CDFML_DATA_FILE);
		if(!(cdfmlDataFile instanceof File)) {
			String cdfmlDataFileName = cdfDataFileNameMatcher.group(1) + FILE_NAME_SUFFIX_CDFML;
			cdfmlDataFile = new File(scanDataUrl, cdfmlDataFileName);
			request.put(REQUEST_KEY_CDFML_DATA_FILE, cdfmlDataFile);
		}
		
		Matcher cdfmlDataFileNameMatcher;
		if(cdfmlDataFile instanceof File) {	
			cdfmlDataFileNameMatcher = CDFML_DATA_FILE_NAME_PATTERN.matcher(((File)cdfmlDataFile).getName());
			if(!cdfmlDataFileNameMatcher.matches()) {
				throw new ConverterFactoryException("The required CDFML data file does not have a '" + FILE_NAME_SUFFIX_CDFML + "' extension.");
			}
		}
		else {
			throw new ConverterFactoryException("The required CDFML data file not found in converter request.");
		}
		
		return request;
	}
	
	public void setProcessTimeout(long processTimeout) {
		this.processTimeout = processTimeout;
	}

	public void setProcessFactory(ProcessFactory processFactory) {
		this.processFactory = processFactory;
	}

	protected static class CDFtoCDFMLConverter extends AbstractCDFtoCDFMLConverter {
	
		public CDFtoCDFMLConverter(String fromFormat, String toFormat, boolean forceUpdate) {
			super(fromFormat, toFormat, forceUpdate);
		}
	}
}
