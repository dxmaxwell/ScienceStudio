/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFEventElementOptions class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.util.ElementOptions;


/**
 * @author maxweld
 *
 */
public class DAFEventElementOptions extends ElementOptions {

	public static enum MatchType {
		NAME, DESCRIPTION
	}
	
	private MatchType matchType = MatchType.NAME;
	
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}
	
	public int getElementIndex(DAFEvent event) {
		
		Matcher matcher;
		DAFElement element;
		int nElements = event.getNumberOfElements();
		List<DAFElement> elements = event.getElements();
			
		for(int idx=0; idx<nElements; idx++) {
			
			element = elements.get(idx);
			
			for(Pattern option : getOptions()) {
				switch (matchType) {					
					case NAME:
					default:
						matcher = option.matcher(element.getName());
						break;
					
					case DESCRIPTION:
						matcher = option.matcher(element.getDescription());
						break;
				}
				
				if(matcher.matches()) {
					return idx;
				}
			}
		}
		return -1;
	}
}
