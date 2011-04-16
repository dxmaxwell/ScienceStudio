/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonDAO interface.
 *     
 */
package ca.sciencestudio.model.project.dao;

import java.util.List;

import ca.sciencestudio.model.project.ProjectPerson;

/**
 * @author maxweld
 *
 */
public interface ProjectPersonDAO {
	public ProjectPerson createProjectPerson();
	
	public int addProjectPerson(ProjectPerson projectPerson);
	public void editProjectPerson(ProjectPerson projectPerson);
	public void removeProjectPerson(int projectPersonId);
	public void removeProjectPerson(ProjectPerson projectPerson);
	
	public ProjectPerson getProjectPersonById(int projectPersonId);
	public ProjectPerson getProjectPersonByProjectIdAndPersonUid(int projectId, String personUid);
	
	public List<ProjectPerson> getProjectPersonList();
	public List<ProjectPerson> getProjectPersonListByProjectId(int projectId);
	public List<ProjectPerson> getProjectPersonListByPersonUid(String personUid);
}
