/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersToCDFAdapterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.adapter.factory;

import java.io.File;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.vespers.data.converter.adapter.MapXYVespersAdapter;
import ca.sciencestudio.vespers.data.converter.adapter.MapXYVespersToCDFAdapter;

/**
 * 
 * @author maxweld
 *
 */
public class MapXYVespersToCDFAdapterFactory implements MapXYVespersAdapterFactory, StdConverter {

	private static final String SUPPORTED_TO_FORMAT = "CDF"; 
	
//	private static final String CDF_DATA_FILE_NAME_REGEX = String.format("(.*)(%s)", Pattern.quote(FILE_NAME_SUFFIX_CDF));
//	private static final Pattern CDF_DATA_FILE_NAME_PATTERN = Pattern.compile(CDF_DATA_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
	
	private long cdfSpectrumCompressionLevel = MapXYVespersToCDFAdapter.DEFAULT_CDF_SPECTRUM_COMPRESSION_LEVEL;

	@Override
	public boolean supports(ConverterMap request) {
		return SUPPORTED_TO_FORMAT.equals(request.getToFormat());
	}

	@Override
	public MapXYVespersAdapter getAdapter(ConverterMap request) throws ConverterFactoryException {
	
		MapXYVespersToCDFAdapter adapter = new MapXYVespersToCDFAdapter();
		
		adapter.setCdfSpectrumCompressionLevel(cdfSpectrumCompressionLevel);
		adapter.setForceUpdate(request.isForceUpdate());
		
		// Output CDF Data File  //
		Object cdfDataFile = request.get(REQUEST_KEY_CDF_DATA_FILE);
		if(!(cdfDataFile instanceof File)) {
			File dataFile = (File)request.get(REQUEST_KEY_DATA_FILE);
			String dataFileName = dataFile.getName();
			int idx = dataFileName.lastIndexOf(".");
			if(idx > 0) {
				dataFileName = dataFileName.substring(0,idx);
			}
			String cdfDataFileName = dataFileName + FILE_NAME_SUFFIX_CDF;
			cdfDataFile = new File(dataFile.getParent(), cdfDataFileName);
		}
		
		//  Check that data file extension is '.cdf'. Is this needed? //
		//Matcher cdfDataFileNameMatcher;
		//if(cdfDataFile instanceof File) {
		//	cdfDataFileNameMatcher = CDF_DATA_FILE_NAME_PATTERN.matcher(((File)cdfDataFile).getName());
		//	if(!cdfDataFileNameMatcher.matches()) {
		//		throw new ConverterFactoryException("The required CDF data file does not have a '" + FILE_NAME_SUFFIX_CDF + "' extension.");
		//	}
		//}
		//else {
		//	throw new ConverterFactoryException("The required CDF data file not found in converter request.");
		//}

		adapter.setCdfDataFile((File)cdfDataFile);
		adapter.setDataFile((File)request.get(REQUEST_KEY_DATA_FILE));
		adapter.setSpectraFile((File)request.get(REQUEST_KEY_SPECTRA_FILE));
		return adapter;
	}
	
	public void setCdfSpectrumCompressionLevel(long cdfSpectrumCompressionLevel) {
		this.cdfSpectrumCompressionLevel = cdfSpectrumCompressionLevel;
	}
}
