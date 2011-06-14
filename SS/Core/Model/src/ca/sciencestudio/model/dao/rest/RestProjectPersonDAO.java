/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectPersonDAO class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.ProjectPerson;
import ca.sciencestudio.model.dao.ProjectPersonDAO;
import ca.sciencestudio.model.dao.rest.support.AbstractRestModelDAO;
import ca.sciencestudio.model.dao.rest.support.RestProjectPerson;
import ca.sciencestudio.model.dao.support.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
public class RestProjectPersonDAO extends AbstractRestModelDAO<ProjectPerson, RestProjectPerson> implements ProjectPersonDAO {

	
	@Override
	public List<ProjectPerson> getAllByPersonGid(Object personGid) {
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = Arrays.asList(getRestTemplate().getForObject(getModelUrl("?personGid={1}"), getModelArrayClass(), personGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Project Person list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Project Persons with Person GID: " + personGid + ", size: " + projectPersons.size());
		}
		return Collections.unmodifiableList(projectPersons);
	}
	
	@Override
	public List<ProjectPerson> getAllByProjectGid(Object projectGid) {
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = Arrays.asList(getRestTemplate().getForObject(getModelUrl("?projectGid={1}"), getModelArrayClass(), projectGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Project Person list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Project Persons with Project GID: " + projectGid + ", size: " + projectPersons.size());
		}
		return Collections.unmodifiableList(projectPersons);
	}
	
	@Override
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(Object projectGid, Object personGid) {
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = Arrays.asList(getRestTemplate().getForObject(getModelUrl("?projectGid={1}&personGid={2}"), getModelArrayClass(), projectGid, personGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Project Person list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Project Persons with Project GID: " + projectGid + ", and Person GID: " + personGid + ", size: " + projectPersons.size());
		}
		return Collections.unmodifiableList(projectPersons);
	}

	@Override
	protected RestProjectPerson toRestModel(ProjectPerson projectPerson) {
		RestProjectPerson restProjectPerson = new RestProjectPerson();
		restProjectPerson.setPersonGid(projectPerson.getPersonGid());
		restProjectPerson.setProjectId(projectPerson.getProjectId());
		restProjectPerson.setRole(projectPerson.getRole());
		return restProjectPerson;
	}

	@Override
	protected String getModelUrl() {
		return getBaseUrl() + "/project/persons";
	}

	@Override
	protected Class<ProjectPerson> getModelClass() {
		return ProjectPerson.class;
	}

	@Override
	protected Class<ProjectPerson[]> getModelArrayClass() {
		return ProjectPerson[].class;
	}
}
