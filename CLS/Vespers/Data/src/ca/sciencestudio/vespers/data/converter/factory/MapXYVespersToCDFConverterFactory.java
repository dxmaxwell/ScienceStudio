/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersToCDFConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.vespers.data.converter.MapXYVespersToCDFConverter;

/**
 * @author maxweld
 *
 */
public class MapXYVespersToCDFConverterFactory extends MapXYVespersConverterFactory {

	private static final String CDF_DATA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_CDF));
	
	private static final Pattern CDF_DATA_FILE_NAME_PATTERN = Pattern.compile(CDF_DATA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	
	private static final String SUPPORTED_FROM_FORMAT = "DAF";
	private static final String SUPPORTED_TO_FORMAT = "CDF";
	
	private long cdfSpectrumCompressionLevel = MapXYVespersToCDFConverter.DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL;
	
	@Override
	public Converter getConverter(ConverterMap request) throws ConverterFactoryException {
		
		ConverterMap validRequest = validateRequest(request);
		boolean forceUpdate = validRequest.isForceUpdate();
		String fromFormat = validRequest.getFromFormat();
		String toFormat = validRequest.getToFormat();
		
		MapXYVespersToCDFConverter converter = new MapXYVespersToCDFConverter(fromFormat, toFormat, forceUpdate);
		prepareConverter(converter, validRequest);
		return converter;
	}
	
	@Override
	public ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {
		
		if(!SUPPORTED_FROM_FORMAT.equals(request.getFromFormat())) {
			throw new ConverterFactoryException("Convert FROM format, " + request.getFromFormat() + ", not supported.");
		}
		
		if(!SUPPORTED_TO_FORMAT.equals(request.getToFormat())) {
			throw new ConverterFactoryException("Convert TO format, " + request.getToFormat() + ", not supported.");
		}
		
		// Need to call this first to get DAF data file. //
		request = super.validateRequest(request);
		
		// CDF Data File //
		Object cdfDataFile = request.get(REQUEST_KEY_CDF_DATA_FILE);
		if(!(cdfDataFile instanceof File)) {
			File dafDataFile = (File)request.get(REQUEST_KEY_DAF_DATA_FILE);
			String dafDataFileName = dafDataFile.getName();
			String cdfDataFileName =  dafDataFileName + FILE_NAME_SUFFIX_CDF;
			int idx = dafDataFileName.lastIndexOf(FILE_NAME_SUFFIX_DAF_DATA);
			if(idx > 0) {
				cdfDataFileName = dafDataFileName.substring(0,idx) + FILE_NAME_SUFFIX_CDF;
			}
			cdfDataFile = new File(dafDataFile.getParent(), cdfDataFileName);
			request.put(REQUEST_KEY_CDF_DATA_FILE, cdfDataFile);
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
		
		return request;
	}
	
	public void prepareConverter(MapXYVespersToCDFConverter converter, ConverterMap request) throws ConverterFactoryException {
	
		File cdfDataFile = (File)request.get(REQUEST_KEY_CDF_DATA_FILE);
		
		converter.setCdfDataFile(cdfDataFile);
		
		converter.setCdfSpectrumCompressionLevel(cdfSpectrumCompressionLevel);
	
		super.prepareConverter(converter, request);
	}
	
	public long getCdfSpectrumCompressionLevel() {
		return cdfSpectrumCompressionLevel;
	}
	public void setCdfSpectrumCompressionLevel(long cdfSpectrumCompressionLevel) {
		this.cdfSpectrumCompressionLevel = cdfSpectrumCompressionLevel;
	}
}
