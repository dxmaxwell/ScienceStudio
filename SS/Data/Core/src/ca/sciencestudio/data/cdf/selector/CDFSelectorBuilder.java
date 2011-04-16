/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFSelectorBuilder abstract class.
 *     
 */
package ca.sciencestudio.data.cdf.selector;

import ca.sciencestudio.data.cdf.selector.LogicalAndCDFSelector;
import ca.sciencestudio.data.cdf.selector.LogicalOrCDFSelector;
import ca.sciencestudio.data.cdf.selector.EqualIntCDFSelector;
import ca.sciencestudio.data.cdf.selector.LessThanIntCDFSelector;
import ca.sciencestudio.data.cdf.selector.GreaterThanIntCDFSelector;
import ca.sciencestudio.data.cdf.selector.LessThanOrEqualIntCDFSelector;
import ca.sciencestudio.data.cdf.selector.GreaterThanOrEqualIntCDFSelector;
import ca.sciencestudio.data.cdf.selector.CDFSelector;

/**
 * @author maxweld
 *
 */
public abstract class CDFSelectorBuilder {

	public static CDFSelector AND(CDFSelector cdfSelector1, CDFSelector cdfSelector2) {
		return new LogicalAndCDFSelector(cdfSelector1, cdfSelector2);
	}
	
	public static CDFSelector OR(CDFSelector cdfSelector1, CDFSelector cdfSelector2) {
		return new LogicalOrCDFSelector(cdfSelector1, cdfSelector2);
	}

	public static CDFSelector EQ(String cdfVarName, String value) {
		return new EqualStringCDFSelector(cdfVarName, value);
	}
	
	public static CDFSelector EQ(String cdfVarName, int value) {
		return new EqualIntCDFSelector(cdfVarName, value);
	}
	
	public static CDFSelector LT(String cdfVarName, int value) {
		return new LessThanIntCDFSelector(cdfVarName, value);
	}
	
	public static CDFSelector GT(String cdfVarName, int value) {
		return new GreaterThanIntCDFSelector(cdfVarName, value);
	}
	
	public static CDFSelector LE(String cdfVarName, int value) {
		return new LessThanOrEqualIntCDFSelector(cdfVarName, value);
	}
	
	public static CDFSelector GE(String cdfVarName, int value) {
		return new GreaterThanOrEqualIntCDFSelector(cdfVarName, value);
	}
}
