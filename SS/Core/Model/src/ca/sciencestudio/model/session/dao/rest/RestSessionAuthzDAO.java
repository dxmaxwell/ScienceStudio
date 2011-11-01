/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSessionAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestSession;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.RestAuthoritiesModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class RestSessionAuthzDAO extends RestAuthoritiesModelAuthzDAO<Session> implements SessionAuthzDAO {

	public static final String SESSION_AUTHZ_PATH = "/authz/sessions";
	public static final String SESSION_MODEL_PATH = "/model/sessions";
	
	@Override
	public Data<List<Session>> getAllByProjectGid(String user, String projectGid) {
		List<Session> sessions;
		try {
			sessions = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "user={user}", "project={project}"), getModelArrayClass(), user, projectGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Session>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + sessions.size());
		}
		return new SimpleData<List<Session>>(Collections.unmodifiableList(sessions));
	}

	@Override
	protected Object toRestModel(Session session) {
		RestSession restSession = new RestSession();
		restSession.setProjectGid(session.getProjectGid());
		restSession.setLaboratoryGid(session.getLaboratoryGid());
		restSession.setName(session.getName());
		restSession.setDescription(session.getDescription());
		restSession.setProposal(session.getProposal());
		restSession.setStartDate(session.getStartDate());
		restSession.setEndDate(session.getEndDate());
		return restSession;
	}
	
	@Override
	protected String getAuthzPath() {
		return SESSION_AUTHZ_PATH;
	}

	@Override
	protected String getModelPath() {
		return SESSION_MODEL_PATH;
	}

	@Override
	protected Class<Session> getModelClass() {
		return Session.class;
	}

	@Override
	protected Class<Session[]> getModelArrayClass() {
		return Session[].class;
	}
}
