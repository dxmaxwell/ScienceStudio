/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectDAO class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.rest.support.AbstractRestModelDAO;
import ca.sciencestudio.model.dao.rest.support.RestProject;

/**
 * @author maxweld
 *
 */
public class RestProjectDAO extends AbstractRestModelDAO<Project, RestProject> implements ProjectDAO {
		
	@Override
	protected RestProject toRestModel(Project project) {
		RestProject restProject = new RestProject();
		restProject.setName(project.getName());
		restProject.setDescription(project.getDescription());
		restProject.setStartDate(project.getStartDate());
		restProject.setEndDate(project.getEndDate());
		restProject.setStatus(project.getStatus());	
		return restProject;
	}

	@Override
	protected String getModelUrl() {
		return getBaseUrl() + "/projects";
	}
	
	@Override
	protected Class<Project> getModelClass() {
		return Project.class;
	}
	
	@Override
	protected Class<Project[]> getModelArrayClass() {
		return Project[].class;
	}
}
