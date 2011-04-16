/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPerson interface.
 *     
 */
package ca.sciencestudio.model.project;

import java.io.Serializable;

import ca.sciencestudio.model.project.ProjectRole;

/**
 * @author maxweld
 *
 */
public interface ProjectPerson extends Serializable {

	public int getId();
	public void setId(int id);
	
	public int getProjectId();
	public void setProjectId(int projectId);
	
	public String getPersonUid();
	public void setPersonUid(String personUid);
	
	public ProjectRole getProjectRole();
	public void setProjectRole(ProjectRole projectRole);
}
