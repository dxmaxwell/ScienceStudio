/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LessThanOrEqualIntCDFSelector class.
 *     
 */
package ca.sciencestudio.data.cdf.selector;

import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.support.CDFSelectorException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public class LessThanOrEqualIntCDFSelector extends CDFSelector {
	
	private String cdfVarName;
	private int value;
	
	public LessThanOrEqualIntCDFSelector(String cdfVarName, int value) {
		this.cdfVarName = cdfVarName;
		this.value = value;
	}
	
	@Override
	public boolean select(CDFRecord cdfRecord) throws CDFSelectorException {
		try {
			return (cdfRecord.getIntByName(cdfVarName) <= value);
		}
		catch(RecordFormatException e) {
			throw new CDFSelectorException("", e);
		}
	}
}
