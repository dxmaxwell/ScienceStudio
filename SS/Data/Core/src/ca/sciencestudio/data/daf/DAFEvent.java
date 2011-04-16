/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AcqDatEvent class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.daf.DAFElement;
import ca.sciencestudio.data.daf.DAFVariable;
import ca.sciencestudio.util.text.PeekableBufferedReader;

/**
 * @author maxweld
 *
 */
public class DAFEvent {
	
	static private final String EVENT_ID_REGEX_FORMAT = "\\((%s)\\)";
	static private final String SHORT_EVENT_NAMES_REGEX_FORMAT = "^#%s(\\s+%s)*";
	static private final String SHORT_EVENT_DESCRIPTIONS_REGEX_FORAMT = "^#%s(\\s+(%s)){%d}";
	
	static private final String EVENT_ID_REGEX = String.format(EVENT_ID_REGEX_FORMAT, "\\d+");
	static private final String EVENT_NAME_REGEX = "([.:\\-\\w\\[\\]]+)";
	static private final String EVENT_DESCRIPTION_REGEX = "([^\\s\"]+)|\"([^\"]*)\"";
	
	static private final Pattern EVENT_ID_PATTERN = Pattern.compile(EVENT_ID_REGEX);
	static private final Pattern EVENT_NAME_PATTERN = Pattern.compile(EVENT_NAME_REGEX);
	static private final Pattern EVENT_DESCRIPTION_PATTERN = Pattern.compile(EVENT_DESCRIPTION_REGEX);
	
	private int id;
	private List<DAFVariable> variables = new ArrayList<DAFVariable>();
	
	protected Log log = LogFactory.getLog(getClass()); 
	
	public static DAFEvent parseEvent(PeekableBufferedReader reader) throws IOException {
		return parseShortEvent(reader);
	}
	
	private static DAFEvent parseShortEvent(PeekableBufferedReader reader) throws IOException {
		
		Pattern eventIdPattern  = EVENT_ID_PATTERN;
		Pattern eventNamePattern = EVENT_NAME_PATTERN;
		
		String shortEventNamesRegex = String.format(SHORT_EVENT_NAMES_REGEX_FORMAT, 
														eventIdPattern, eventNamePattern);
		Pattern shortEventNamesPattern = Pattern.compile(shortEventNamesRegex);
				
		if(shortEventNamesPattern.matcher(reader.peekLine()).matches()) {
				
			Matcher matcher = eventIdPattern.matcher(reader.readLine());
			if(matcher.find()) {
				
				DAFEvent event = new DAFEvent(Integer.parseInt(matcher.group(1)));	
				
				matcher.usePattern(eventNamePattern);
				while(matcher.find()) {
					event.addVariable(new DAFVariable(matcher.group(1)));
				}
				
				return event;
			}
		}
		return null;
	}
	
	protected DAFEvent(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumberOfVariables() {
		return variables.size();
	}
	
	public List<DAFVariable> getVariables() {
		return Collections.unmodifiableList(variables);
	}
	
	protected void addVariable(DAFVariable variable) {
		if(!variables.contains(variable)) {
			variables.add(variable);
		}
	}
	
	public int getVariableIndexByName(String name) {
		int variableIdx = 0;
		for(DAFVariable variable : variables) {
			if(variable.getName().equals(name)) {
				return variableIdx;
			}
			variableIdx++;
		}
		return -1;
	}
	
	public DAFVariable getVariableByName(String name) {
		try {
			return variables.get(getVariableIndexByName(name));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getVariableIndexByDescription(String description) {
		int variableIdx = 0;
		for(DAFVariable variable : variables) {
			if(variable.getDescription().equals(description)) {
				return variableIdx;
			}
			variableIdx++;
		}
		return -1;
	}
	
	public DAFVariable getVariableByDescription(String description) {
		try {
			return variables.get(getVariableIndexByDescription(description));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getNumberOfElements() {
		return getElements().size();
	}
	
	public List<DAFElement> getElements() {
		List<DAFElement> elements = new ArrayList<DAFElement>();
		for(DAFVariable variable : variables) {
			elements.addAll(variable.getElements());
		}
		return Collections.unmodifiableList(elements);
	}
		
	public int getElementIndexByName(String name) {
		int elementIdx = 0;
		for(DAFElement element : getElements()) {
			if(element.getName().equals(name)) {
				return elementIdx;
			}
			elementIdx++;
		}
		return -1;
	}
	
	public DAFElement getElementByName(String name) {
		try {
			return getElements().get(getElementIndexByName(name));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getElementIndexByDescription(String description) {
		int elementIdx = 0;
		for(DAFElement element : getElements()) {
			if(element.getDescription().equals(description)) {
				return elementIdx;
			}
			elementIdx++;
		}
		return -1;
	}
	
	public DAFElement getElementByDescription(String description) {
		try {
			return getElements().get(getElementIndexByDescription(description));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public boolean parseUpdate(PeekableBufferedReader reader) throws IOException {
		
		String eventIdRegex = String.format(EVENT_ID_REGEX_FORMAT, String.valueOf(id));
		Pattern eventIdPattern = Pattern.compile(eventIdRegex);
		Pattern eventDescriptionPattern = EVENT_DESCRIPTION_PATTERN;
				
		String shortEventDescriptionsRegex = String.format(SHORT_EVENT_DESCRIPTIONS_REGEX_FORAMT, 
										eventIdPattern, eventDescriptionPattern, getNumberOfVariables());		
		Pattern shortEventDescriptionsPattern = Pattern.compile(shortEventDescriptionsRegex);
		
		if(shortEventDescriptionsPattern.matcher(reader.peekLine()).matches()) {
			
			Matcher matcher = eventIdPattern.matcher(reader.readLine());
			if(matcher.find()) {
				matcher.usePattern(eventDescriptionPattern);
				for(int idx = 0; matcher.find(); idx++) {
					if(matcher.group(1) != null) {
						variables.get(idx).setDescription(matcher.group(1));
					}
					else if(matcher.group(2) != null) {
						variables.get(idx).setDescription(matcher.group(2));
					}
					else {
						variables.get(idx).setDescription("N/A");
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public String toString(boolean descriptions) {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for(DAFVariable variable : variables) {
			if(first) { first = false; }
			else { buffer.append(", "); }
			buffer.append(variable.toString(descriptions));
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		return toString(true);
	}
}