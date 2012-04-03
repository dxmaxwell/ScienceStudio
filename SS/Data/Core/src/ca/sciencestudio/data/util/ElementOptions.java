/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ElementOptions class.
 *     
 */
package ca.sciencestudio.data.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author maxweld
 *
 */
public class ElementOptions {

	private Set<Pattern> options = new LinkedHashSet<Pattern>();
	
	public int getElementIndex(String[] elements) {
		int nElements = elements.length;
		for(int idx=0; idx<nElements; idx++) {
			for(Pattern option : options) {
				if(option.matcher(elements[idx]).matches()) {
					return idx;
				}
			}
		}
		return -1;
	}
	
	public Set<Pattern> getOptions() {
		return options;
	}
	
	public void setOptions(Set<Pattern> options) {
		this.options = options;
	}
	
	public void setOptionsExact(Collection<String> optionsExact) {
		List<String> optionsRegex = new ArrayList<String>();
		for(String optionExact : optionsExact) {
			optionsRegex.add(Pattern.quote(optionExact));
		}
		setOptionsRegex(optionsRegex);
	}
	
	public void setOptionsRegex(Collection<String> optionsRegex) {
		Set<Pattern> options = new LinkedHashSet<Pattern>();
		for(String optionRegex : optionsRegex) {
			options.add(Pattern.compile(optionRegex));
		}
		setOptions(options);
	}
}
