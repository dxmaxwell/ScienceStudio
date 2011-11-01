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
	public static final Role DEFAULT_ROLE = null;
	
	private String gid;
	private String personGid;
	private String projectGid;
	private Role role;
	
	public ProjectPerson() {
		gid = DEFAULT_GID;
		personGid = DEFAULT_PERSON_GID;
		projectGid = DEFAULT_PROJECT_GID;
		role = DEFAULT_ROLE;
	}
	
	public ProjectPerson(ProjectPerson projectPerson) {
		gid = projectPerson.getGid();
		personGid = projectPerson.getPersonGid();
		projectGid = projectPerson.getProjectGid();
		role = projectPerson.getRole();
	}
	
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

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
