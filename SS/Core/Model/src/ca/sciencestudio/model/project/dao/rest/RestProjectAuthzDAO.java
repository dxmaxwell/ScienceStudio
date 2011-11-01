/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.project.dao.rest.support.RestProject;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.RestAuthoritiesModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class RestProjectAuthzDAO extends RestAuthoritiesModelAuthzDAO<Project> implements ProjectAuthzDAO {
	
	public static final String PROJECT_AUTHZ_PATH = "/authz/projects";
	public static final String PROJECT_MODEL_PATH = "/model/projects";
	
	@Override
	public Data<List<Project>> getAll(String personGid) {
		List<Project> projects;
		try {
			projects = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "user={user}"), getModelArrayClass(), personGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Project>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects, size: " + projects.size());
		}
		return new SimpleData<List<Project>>(Collections.unmodifiableList(projects));
	}

	@Override
	protected RestProject toRestModel(Project project) {
		RestProject restProject = new RestProject();
		restProject.setName(project.getName());
		restProject.setDescription(project.getDescription());
		restProject.setFacilityGid(project.getFacilityGid());
		restProject.setStartDate(project.getStartDate());
		restProject.setEndDate(project.getEndDate());
		restProject.setStatus(project.getStatus());	
		return restProject;
	}

	@Override
	protected String getAuthzPath() {
		return PROJECT_AUTHZ_PATH;
	}
	
	@Override
	protected String getModelPath() {
		return PROJECT_MODEL_PATH;
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
