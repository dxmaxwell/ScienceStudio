/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     EqualStringCDFSelector class.
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
public class EqualStringCDFSelector extends CDFSelector {

	private String cdfVarName;
	private String value;
	
	public EqualStringCDFSelector(String cdfVarName, String value) {
		this.cdfVarName = cdfVarName;
		this.value = value;
	}
	
	@Override
	public boolean select(CDFRecord cdfRecord) throws CDFSelectorException {
		try {
			return (cdfRecord.getStringByName(cdfVarName).equals(value));
		}
		catch(RecordFormatException e) {
			throw new CDFSelectorException("", e);
		}
	}
}
