/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LogicalAndCDFSelector class.
 *     
 */
package ca.sciencestudio.data.cdf.selector;

import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.support.CDFSelectorException;

/**
 * @author maxweld
 *
 */
public class LogicalAndCDFSelector extends CDFSelector {

	private CDFSelector cdfSelector1;
	private CDFSelector cdfSelector2;
	
	public LogicalAndCDFSelector(CDFSelector cdfSelector1, CDFSelector cdfSelector2) {
		this.cdfSelector1 = cdfSelector1;
		this.cdfSelector2 = cdfSelector2;
	}
	
	@Override
	public boolean select(CDFRecord cdfRecord) throws CDFSelectorException {
		return (cdfSelector1.select(cdfRecord) && cdfSelector2.select(cdfRecord));
	}
}
