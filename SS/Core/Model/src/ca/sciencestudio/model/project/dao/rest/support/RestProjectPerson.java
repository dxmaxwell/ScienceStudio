/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectPerson class.
 *     
 */
package ca.sciencestudio.model.project.dao.rest.support;

import ca.sciencestudio.model.project.ProjectPerson;

/**
 * @author maxweld
 *
 *
 */
public class RestProjectPerson {

	private String personGid = ProjectPerson.DEFAULT_PERSON_GID;
	private int projectId = ProjectPerson.DEFAULT_PROJECT_ID;
	private String role = ProjectPerson.DEFAULT_ROLE;
	
	public String getPersonGid() {
		return personGid;
	}
	public void setPersonGid(String personGid) {
		this.personGid = personGid;
	}
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
