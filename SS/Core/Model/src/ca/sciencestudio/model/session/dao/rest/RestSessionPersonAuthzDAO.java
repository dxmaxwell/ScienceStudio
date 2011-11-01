/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSessionPersonAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonAuthzDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestSessionPerson;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class RestSessionPersonAuthzDAO extends AbstractRestModelAuthzDAO<SessionPerson> implements SessionPersonAuthzDAO {

	public static final String SESSION_PERSON_MODEL_PATH = "/model/session/persons";
	
	@Override
	public Data<List<SessionPerson>> getAllBySessionGid(String user, String sessionGid) {
		List<SessionPerson> sessionPersons;
		try {
			sessionPersons = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "user={user}", "session={session}"), getModelArrayClass(), user, sessionGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<SessionPerson>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all SessionPersons by Session GID: " + sessionGid + ", size: " + sessionPersons.size());
		}
		return new SimpleData<List<SessionPerson>>(Collections.unmodifiableList(sessionPersons));
	}

	@Override
	protected Object toRestModel(SessionPerson sessionPerson) {
		RestSessionPerson restSessionPerson = new RestSessionPerson();
		restSessionPerson.setPersonGid(sessionPerson.getPersonGid());
		restSessionPerson.setSessionGid(sessionPerson.getSessionGid());
		restSessionPerson.setRole(sessionPerson.getRole());
		return restSessionPerson;
	}

	@Override
	protected String getModelPath() {
		return SESSION_PERSON_MODEL_PATH;
	}

	@Override
	protected Class<SessionPerson> getModelClass() {
		return SessionPerson.class;
	}

	@Override
	protected Class<SessionPerson[]> getModelArrayClass() {
		return SessionPerson[].class;
	}
}
