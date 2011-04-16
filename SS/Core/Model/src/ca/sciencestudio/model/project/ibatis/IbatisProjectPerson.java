/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectPerson class.
 *     
 */
package ca.sciencestudio.model.project.ibatis;

import ca.sciencestudio.model.project.ProjectRole;
import ca.sciencestudio.model.project.ProjectPerson;

/**
 * @author maxweld
 *
 */
public class IbatisProjectPerson implements ProjectPerson {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int projectId;
	private String personUid;
	private ProjectRole projectRole;
	
	public String getProjectRoleString() {
		return projectRole.name();
	}
	public void setProjectRoleString(String projectRole) {
		try {
			this.projectRole = ProjectRole.valueOf(projectRole);
		}
		catch(IllegalArgumentException e) {
			this.projectRole = ProjectRole.UNKNOWN;
		}
	}
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getProjectId() {
		return projectId;
	}
	@Override
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public String getPersonUid() {
		return personUid;
	}
	@Override
	public void setPersonUid(String personUid) {
		this.personUid = personUid;
	}
	
	@Override
	public ProjectRole getProjectRole() {
		return projectRole;
	}
	@Override
	public void setProjectRole(ProjectRole projectRole) {
		this.projectRole = projectRole;
	}
}
