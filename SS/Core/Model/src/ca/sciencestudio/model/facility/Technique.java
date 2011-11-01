/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Technique class.
 *     
 */
package ca.sciencestudio.model.facility;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class Technique implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "T";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_LONG_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	
	private String gid;
	private String name;
	private String longName;
	private String description;
	
	public Technique() {
		gid = DEFAULT_GID;
		name = DEFAULT_NAME;
		longName = DEFAULT_LONG_NAME;
		description = DEFAULT_DESCRIPTION;
	}
	
	public Technique(Technique t) {
		gid = t.getGid();
		name = t.getName();
		longName = t.getLongName();
		description = t.getDescription();
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
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
