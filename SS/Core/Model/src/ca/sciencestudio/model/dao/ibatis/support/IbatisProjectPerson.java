/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectPerson class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis.support;

import ca.sciencestudio.model.ProjectPerson;

/**
 * @author maxweld
 *
 *
 */
public class IbatisProjectPerson {

	public static final int DEFAULT_ID = 0;
	
	private int id = DEFAULT_ID;
	private String personGid = ProjectPerson.DEFAULT_PERSON_GID;
	private int projectId = ProjectPerson.DEFAULT_PROJECT_ID;
	private String role = ProjectPerson.DEFAULT_ROLE;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
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
