/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFRegexElementParser class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author maxweld
 *
 */
public class DAFRegexElementParser {

	static private final int DEFAULT_NUMBER_OF_ELEMENTS = 0;
	static private final String DEFAULT_ELEMENT_REGEX = "";
	
	private Pattern elementPattern;
	private int numberOfElements;
	
	public DAFRegexElementParser() {
		this(DEFAULT_ELEMENT_REGEX);
	}
	
	public DAFRegexElementParser(String elementRegex) {
		this(elementRegex, DEFAULT_NUMBER_OF_ELEMENTS);
	}
	
	public DAFRegexElementParser(String elementRegex, int numberOfElements) {
		this(Pattern.compile(elementRegex), numberOfElements);
	}
	
	public DAFRegexElementParser(Pattern elementPattern) {
		this(elementPattern, DEFAULT_NUMBER_OF_ELEMENTS);
	}
	
	public DAFRegexElementParser(Pattern elementPattern, int numberOfElements) {
		this.elementPattern = elementPattern;
		this.numberOfElements = numberOfElements;	
	}
	
	public List<String> parseElements(Matcher matcher) {
	
		if(matcher == null) { 
			return null;
		}
		
		matcher.usePattern(elementPattern);
		
		List<String> values = new ArrayList<String>(numberOfElements);
		
		int findCount = 0;
		int groupCount = 0;
		while(matcher.find()) {
			findCount++;
			groupCount = matcher.groupCount();
			if(groupCount > 0) {
				for(int c=1; c<=groupCount; c++) {
					if(matcher.group(c) != null) {
						values.add(matcher.group(c));
						break;
					}
				}
			}
			else {
				values.add(matcher.group());
			}
			
			if(findCount == numberOfElements) {
				break;
			}
		}
		
		if(findCount == 0) {
			return null;
		}
		
		if((numberOfElements > 0) && (numberOfElements != findCount)) {
			return null;
		}
		
		return values;
	}
	
	public void setElementRegex(String elementRegex) {
		setElementPattern(Pattern.compile(elementRegex));
	}
	
	public String getElementRegex() {
		return getElementPattern().pattern();
	}
	
	public Pattern getElementPattern() {
		return elementPattern;
	}
	
	public void setElementPattern(Pattern elementPattern) {
		this.elementPattern = elementPattern;
	}
	
	public int getNumberOfElements() {
		return numberOfElements;
	}
	
	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
}
