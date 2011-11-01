/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Instrument interface.
 *     
 */
package ca.sciencestudio.model.facility;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 */
public class Instrument implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "I";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_LABORATORY_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_LONG_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	
	private String gid;
	private String laboratoryGid;
	private String name;
	private String longName;
	private String description;
	
	public Instrument() {
		gid = DEFAULT_GID;
		laboratoryGid = DEFAULT_LABORATORY_GID;
		name = DEFAULT_NAME;
		longName = DEFAULT_LONG_NAME;
		description = DEFAULT_DESCRIPTION;
	}
	
	public Instrument(Instrument instrument) {
		gid = instrument.getGid();
		laboratoryGid = instrument.getLaboratoryGid();
		name = instrument.getName();
		longName = instrument.getLongName();
		description = instrument.getDescription();
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
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
