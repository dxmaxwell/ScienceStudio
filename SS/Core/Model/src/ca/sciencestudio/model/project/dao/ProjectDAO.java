/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectDAO interface.
 *     
 */

package ca.sciencestudio.model.project.dao;

import java.util.List;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectStatus;

/**
 * @author maxweld
 * 
 */
public interface ProjectDAO {
	public Project createProject();
	
	public int addProject(Project project);
	public void editProject(Project project);
	public void removeProject(int projectId);
	public void removeProject(Project project);
	
	public Project getProjectById(int projectId);
	public Project getProjectByScanId(int scanId);
	public Project getProjectBySessionId(int sessionId);
	public Project getProjectByExperimentId(int experimentId);
	
	public List<Project> getProjectList();
	public List<Project> getProjectListByPersonUid(String personUid);
	public List<Project> getProjectListByStatus(ProjectStatus status);
	public List<Project> getProjectListByPersonUidAndStatus(String personUid, ProjectStatus status);	
}
