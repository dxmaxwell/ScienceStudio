/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AcqDatElement class.
 *     
 */
package ca.sciencestudio.data.daf;

import ca.sciencestudio.data.daf.DAFVariable;

/**
 * @author maxweld
 *
 */
public class DAFElement {

	private static final int DEFAULT_ARRAY_INDEX = -1;
	
	private DAFVariable variable;
	
	private int dataIndex;

	public DAFElement(DAFVariable variable) {
		this(variable, DEFAULT_ARRAY_INDEX);
	}
	public DAFElement(DAFVariable variable, int dataIndex) {
		this.variable = variable;
		this.dataIndex = dataIndex;
	}
	
	public int getDataIndex() {
		return dataIndex;
	}
	
	public String getName() {
		String name = variable.getName();
		if(variable.hasDataIndex()) {
			name += "[" + dataIndex + "]";		
		}
		return name;
	}
	
	public String getDescription() {
		String description = variable.getDescription();
		if(variable.hasDataIndex()) {
			description += "[" + dataIndex + "]";
		}
		return description;		
	}
	
	public String toString(boolean description) {
		StringBuffer buffer = new StringBuffer(getName());
		if(description) {
			buffer.append(" \"");
			buffer.append(getDescription());
			buffer.append("\"");
		}
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		return toString(true);
	}
}
