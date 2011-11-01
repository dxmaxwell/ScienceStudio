/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProjectPersonDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.model.project.dao.rest.support.RestProjectPerson;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
public class RestProjectPersonAuthzDAO extends AbstractRestModelAuthzDAO<ProjectPerson> implements ProjectPersonAuthzDAO {
	
	public static final String PROJECT_PERSON_MODEL_PATH = "/project/persons";
	
	@Override
	public Data<List<ProjectPerson>> getAllByPersonGid(String user, String personGid) {
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "user={user}", "person={person}"), getModelArrayClass(), user, personGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Project Person list: " + e.getMessage());
			return new SimpleData<List<ProjectPerson>>(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all ProjectPersons with Person GID: " + personGid + ", size: " + projectPersons.size());
		}
		
		return new SimpleData<List<ProjectPerson>>(Collections.unmodifiableList(projectPersons));
	}
	
	@Override
	public Data<List<ProjectPerson>> getAllByProjectGid(String user, String projectGid) {
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "user={user}", "project={project}"), getModelArrayClass(), user, projectGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Project Person list: " + e.getMessage());
			return new SimpleData<List<ProjectPerson>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all ProjectPersons with Project GID: " + projectGid + ", size: " + projectPersons.size());
		}
		
		return new SimpleData<List<ProjectPerson>>(Collections.unmodifiableList(projectPersons));
	}

	@Override
	protected RestProjectPerson toRestModel(ProjectPerson projectPerson) {
		RestProjectPerson restProjectPerson = new RestProjectPerson();
		restProjectPerson.setPersonGid(projectPerson.getPersonGid());
		restProjectPerson.setProjectGid(projectPerson.getProjectGid());
		restProjectPerson.setRole(projectPerson.getRole());
		return restProjectPerson;
	}

	@Override
	protected String getModelPath() {
		return PROJECT_PERSON_MODEL_PATH;
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
