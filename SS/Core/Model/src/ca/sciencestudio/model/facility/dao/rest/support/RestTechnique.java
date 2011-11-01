/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestTechnique class.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest.support;

import ca.sciencestudio.model.facility.Technique;

/**
 * 
 * @author maxweld
 *
 */
public class RestTechnique {

	private String name = Technique.DEFAULT_NAME;
	private String longName = Technique.DEFAULT_LONG_NAME;
	private String description = Technique.DEFAULT_DESCRIPTION;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
