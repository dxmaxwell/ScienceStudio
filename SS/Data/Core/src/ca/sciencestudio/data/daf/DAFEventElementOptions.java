/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFEventElementOptions class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author maxweld
 *
 */
public class DAFEventElementOptions {

	public static enum MatchType {
		NAME, DESCRIPTION
	}
	
	private MatchType matchType = MatchType.NAME;
	private Set<Pattern> options = new LinkedHashSet<Pattern>();
	
	public void setOptionsExact(Collection<String> optionsExact) {
		List<String> optionsRegex = new ArrayList<String>();
		for(String optionExact : optionsExact) {
			optionsRegex.add(Pattern.quote(optionExact));
		}
		setOptionsRegex(optionsRegex);
	}
	
	public void setOptionsRegex(Collection<String> optionsRegex) {
		options.clear();
		for(String optionRegex : optionsRegex) {
			options.add(Pattern.compile(optionRegex));
		}
	}
	
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
			
			for(Pattern option : options) {
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
