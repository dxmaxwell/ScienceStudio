/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectDAO class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.Project.Status;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.rest.support.RestProject;
import ca.sciencestudio.model.dao.support.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class RestProjectDAO extends AbstractRestModelDAO<Project, RestProject> implements ProjectDAO {
	
	@Override
	public List<Project> getAllByStatus(Status status) {
		return getAllByStatus(status.name());
	}

	@Override
	public List<Project> getAllByStatus(String status) {
		List<Project> projects;
		try {
			projects = Arrays.asList(getRestTemplate().getForObject(getModelUrl("?status={1}"), getModelArrayClass(), status));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects by Status: " + status + ", size: " + projects.size());
		}
		return Collections.unmodifiableList(projects);
	}
	
	@Override
	public List<Project> getAllByPersonGid(Object personGid) {
		List<Project> projects;
		try {
			projects = Arrays.asList(getRestTemplate().getForObject(getModelUrl("?personGid={1}"), getModelArrayClass(), personGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects by Person GID: " + personGid + ", size: " + projects.size());
		}
		return Collections.unmodifiableList(projects);
	}

	@Override
	public List<Project> getAllByPersonGidAndStatus(Object personGid, Status status) {
		return getAllByPersonGidAndStatus(personGid, status.name());
	}

	@Override
	public List<Project> getAllByPersonGidAndStatus(Object personGid, String status) {
		List<Project> projects;
		try {
			projects = Arrays.asList(getRestTemplate().getForObject(getModelUrl("?personGid={1}&status={2}"), getModelArrayClass(), personGid, status));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects by Person GID: " + personGid + ", and Status: " + status + ", size: " + projects.size());
		}
		return Collections.unmodifiableList(projects);
	}
	
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
