/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StdScanParams interface.
 *     
 */
package ca.sciencestudio.data.standard;

/**
 * @author maxweld
 *
 */
public interface StdScanParams {
	
	public static final String PARAM_KEY_DATA_FILE_BASE = "dataFileBase";
	
	public static final String PARAM_KEY_SAMPLE_IMAGE_FILE = "sampleImageFile";
	
	@Deprecated
	public static final String PARAM_KEY_DAF_FILE_NAME = "dafFileName";
	@Deprecated
	public static final String PARAM_KEY_DAF_FILE_VERSION = "dafFileVersion";
	@Deprecated
	public static final String PARAM_KEY_CDF_FILE_NAME = "cdfFileName";
	@Deprecated
	public static final String PARAM_KEY_CDFML_FILE_NAME = "cdfmlFileName";
}
