/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFSelector abstract class.
 *     
 */
package ca.sciencestudio.data.cdf.selector;

import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.support.CDFSelectorException;

/**
 * @author maxweld
 *
 */
public abstract class CDFSelector {
	
	public abstract boolean select(CDFRecord cdfRecord) throws CDFSelectorException;
	
	public CDFSelector AND(CDFSelector cdfSelector) {
		return CDFSelectorBuilder.AND(this, cdfSelector);
	}
	
	public CDFSelector OR(CDFSelector cdfSelector) {
		return CDFSelectorBuilder.OR(this, cdfSelector);
	}
}
