/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectPerson class.
 *     
 */
package ca.sciencestudio.model.project.dao.rest.support;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectPerson.Role;

/**
 * @author maxweld
 *
 *
 */
public class RestProjectPerson {

	private String personGid = ProjectPerson.DEFAULT_PERSON_GID;
	private String projectGid = ProjectPerson.DEFAULT_PROJECT_GID;
	private Role role = ProjectPerson.DEFAULT_ROLE;
	
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
