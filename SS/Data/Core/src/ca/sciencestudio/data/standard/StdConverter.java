/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StdConverter interface.
 *     
 */
package ca.sciencestudio.data.standard;

/**
 * @author maxweld
 *
 */
public interface StdConverter {

	public static final String REQUEST_KEY_FACILITY_NAME = StdConverter.class.getName() + ".facilityName";
	public static final String REQUEST_KEY_FACILITY_LONG_NAME = StdConverter.class.getName() + ".facilityLongName";
	
	public static final String REQUEST_KEY_LABORATORY_NAME = StdConverter.class.getName() + ".laboratoryName";
	public static final String REQUEST_KEY_LABORATORY_LONG_NAME = StdConverter.class.getName() + ".laboratoryLongName";

	public static final String REQUEST_KEY_SAMPLE_NAME = StdConverter.class.getName() + ".sampleName";
	public static final String REQUEST_KEY_SESSION_NAME = StdConverter.class.getName() + ".sessionName";
	public static final String REQUEST_KEY_PROJECT_NAME = StdConverter.class.getName() + ".projectName";
	public static final String REQUEST_KEY_TECHNIQUE_NAME = StdConverter.class.getName() + ".techniqueName";
	public static final String REQUEST_KEY_INSTRUMENT_NAME = StdConverter.class.getName() + ".instrumentName";
	public static final String REQUEST_KEY_EXPERIMENT_NAME = StdConverter.class.getName() + ".experimentName";
	
	public static final String REQUEST_KEY_SCAN_NAME = StdConverter.class.getName() + ".scanName";
	public static final String REQUEST_KEY_SCAN_PARAMS = StdConverter.class.getName() + ".scanParams";
	public static final String REQUEST_KEY_SCAN_DATA_URL = StdConverter.class.getName() + ".scanDataUrl";
	public static final String REQUEST_KEY_SCAN_END_DATE = StdConverter.class.getName() + ".scanEndDate";
	public static final String REQUEST_KEY_SCAN_START_DATE = StdConverter.class.getName() + ".scanStartDate";
	
	public static final String REQUEST_KEY_SCAN = StdConverter.class.getName() + ".scan";
	public static final String REQUEST_KEY_SAMPLE = StdConverter.class.getName() + ".sample";
	public static final String REQUEST_KEY_SESSION = StdConverter.class.getName() + ".session";
	public static final String REQUEST_KEY_PROJECT = StdConverter.class.getName() +  ".project";
	public static final String REQUEST_KEY_FACILITY = StdConverter.class.getName() + ".facility";	
	public static final String REQUEST_KEY_TECHNIQUE = StdConverter.class.getName() + ".technique";
	public static final String REQUEST_KEY_EXPERIMENT = StdConverter.class.getName() + ".experiment";
	public static final String REQUEST_KEY_INSTRUMENT = StdConverter.class.getName() + ".instrument";
	public static final String REQUEST_KEY_LABORATORY = StdConverter.class.getName() + ".laboratory";
	
	public static final String FILE_NAME_SUFFIX_CDF = ".cdf";
	public static final String FILE_NAME_SUFFIX_CDFML = ".cdfml";
	public static final String FILE_NAME_SUFFIX_DAF_DATA = ".dat";
	public static final String FILE_NAME_SUFFIX_DAF_SPEC = "_spectra.dat";
}
