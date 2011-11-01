/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPerson class.
 *     
 */
package ca.sciencestudio.model.project;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class ProjectPerson implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "K";
	
	public static enum Role { OBSERVER, EXPERIMENTER }
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PERSON_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PROJECT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_ROLE = "UNKNOWN";
	
	private String gid = DEFAULT_GID;
	private String personGid = DEFAULT_PERSON_GID;
	private String projectGid = DEFAULT_PROJECT_GID;
	private String role = DEFAULT_ROLE;
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getPersonGid() {
		return personGid;
	}
	public void setPersonGid(String personGid) {
		this.personGid = personGid;
	}
	
	public String getProjectGid() {
		return projectGid;
	}
	public void setProjectGid(String projectGid) {
		this.projectGid = projectGid;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}	
}
