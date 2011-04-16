/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AcqDatVariable class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.daf.DAFElement;

/**
 * @author maxweld
 *
 */
public class DAFVariable {
	
	private static final String REGEX_ARRAY_INDEX_SINGLE = "^(.*)\\[(\\d+)\\]$";
	private static final String REGEX_ARRAY_INDEX_RANGE = "^(.*)\\[(\\d+)-(\\d+)\\]$";
	
	private static final Pattern PATTERN_ARRAY_INDEX_SINGLE = Pattern.compile(REGEX_ARRAY_INDEX_SINGLE);
	private static final Pattern PATTERN_ARRAY_INDEX_RANGE = Pattern.compile(REGEX_ARRAY_INDEX_RANGE);
	
	private boolean dataIndex;
	
	private String name;
	private String description;
	
	private List<DAFElement> elements = new ArrayList<DAFElement>(1);
		
	protected Log log = LogFactory.getLog(getClass());
	
	public DAFVariable(String name) {
		setName(name);
	}

	public DAFVariable(String name, String description) {
		setName(name);
		setDescription(description);
	}
	
	protected String getName() {
		return name;
	}
	
	protected void setName(String name) {
		
		Matcher matcher;
		
		matcher = PATTERN_ARRAY_INDEX_SINGLE.matcher(name);
		if(matcher.matches()) {
			this.name = matcher.group(1);
			this.description = this.name;
			int idx = Integer.parseInt(matcher.group(2));
			this.dataIndex = true;
			this.elements.clear();
			this.elements.add(new DAFElement(this, idx));
			return;
		}
		
		matcher = PATTERN_ARRAY_INDEX_RANGE.matcher(name);
		if(matcher.matches()) {
			this.name = matcher.group(1);
			this.description = this.name;
			int firstArrayIdx = Integer.parseInt(matcher.group(2));
			int lastArrayIdx = Integer.parseInt(matcher.group(3));
			int d = (lastArrayIdx - firstArrayIdx) / Math.abs(lastArrayIdx - firstArrayIdx);
			int n = Math.abs(lastArrayIdx - firstArrayIdx) + 1;
			this.dataIndex = true;
			this.elements.clear();
			for(int idx = 0; idx < n; idx++) {
				this.elements.add(new DAFElement(this, firstArrayIdx + (d * idx)));
			}
			return;
		}
		
		this.name = name;
		this.description = name;
		this.elements.clear();
		this.elements.add(new DAFElement(this));
		return;
	}
	
	protected String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		
		Matcher matcher;
		
		matcher = PATTERN_ARRAY_INDEX_SINGLE.matcher(description);
		if(matcher.matches()) {
			this.description = matcher.group(1);
			int idx = Integer.parseInt(matcher.group(2));
			if(hasDataIndex()) {
				if((idx != getFirstElementDataIndex()) || (idx != getLastElementDataIndex())) {
					log.warn("Description specifies an array with different first or last array index.");
				}
			}
			else {
				this.dataIndex = true;
				this.elements.clear();
				this.elements.add(new DAFElement(this, idx));
			}
			return;
		}
		
		matcher = PATTERN_ARRAY_INDEX_RANGE.matcher(description);
		if(matcher.matches()) {
			this.description = matcher.group(1);
			int firstArrayIdx = Integer.parseInt(matcher.group(2));
			int lastArrayIdx = Integer.parseInt(matcher.group(3));
			if(hasDataIndex()) {
				if((firstArrayIdx != getFirstElementDataIndex() || (lastArrayIdx != getLastElementDataIndex()))) {
					log.warn("Description specifies an array with different first or last array index.");
				}
			}
			else {
				int d = (lastArrayIdx - firstArrayIdx) / Math.abs(lastArrayIdx - firstArrayIdx);
				int n = Math.abs(lastArrayIdx - firstArrayIdx) + 1;
				this.dataIndex = true;
				this.elements.clear();
				for(int idx = 0; idx < n; idx++) {
					this.elements.add(new DAFElement(this, firstArrayIdx + (d * idx)));
				}
			}
			return;
		}
		
		this.description = description;
		return;
	}

	public boolean hasDataIndex() {
		return dataIndex;
	}
	
	public int getNumberOfElements() {
		return elements.size();
	}
	
	public List<DAFElement> getElements() {
		return Collections.unmodifiableList(elements);
	}
	
	public int getFirstElementDataIndex() {
		return elements.get(0).getDataIndex();
	}
	
	public int getLastElementDataIndex() {
		return elements.get(elements.size()-1).getDataIndex();
	}
	
	public int getElementIndexByDataIndex(int dataIndex) {
		int elementIdx = 0;
		for(DAFElement element : elements) {
			if(element.getDataIndex() == dataIndex) {
				return elementIdx;
			}
			elementIdx++;
		}
		return -1;
	}
	
	public DAFElement getElementByDataIndex(int dataIndex) {
		try {
			return elements.get(getElementIndexByDataIndex(dataIndex));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public String toString(boolean descriptions) {
		StringBuffer buffer = new StringBuffer(getName());
		if(descriptions) {
			buffer.append(" \"");
			buffer.append(getDescription());
			buffer.append("\"");
		}
		if(hasDataIndex()) {
			boolean first = true;
			buffer.append(" { ");
			for(DAFElement element : elements) {
				if(first) { first = false; }
				else { buffer.append(", "); }	
				buffer.append(element.toString(descriptions));
			}
			buffer.append(" }");
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		return toString(true);
	}
}
