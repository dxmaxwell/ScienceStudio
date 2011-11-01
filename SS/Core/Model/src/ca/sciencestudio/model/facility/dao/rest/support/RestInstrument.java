/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestInstrument class.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest.support;

import ca.sciencestudio.model.facility.Instrument;

/**
 * @author maxweld
 * 
 *
 */
public class RestInstrument {

	private String laboratoryGid = Instrument.DEFAULT_LABORATORY_GID;
	private String name = Instrument.DEFAULT_NAME;
	private String longName = Instrument.DEFAULT_LONG_NAME;
	private String description = Instrument.DEFAULT_DESCRIPTION;
	
	public String getLaboratoryGid() {
		return laboratoryGid;
	}
	public void setLaboratoryGid(String laboratoryGid) {
		this.laboratoryGid = laboratoryGid;
	}
	
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
