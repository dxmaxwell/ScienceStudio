/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;
import ca.sciencestudio.model.session.validators.SessionPersonValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class SessionPersonAuthzController extends AbstractSessionAuthzController<SessionPerson> {

	private static final String SESSION_PERSON_MODEL_PATH = "/session/persons";

	private SessionBasicDAO sessionBasicDAO;
	
	private SessionPersonBasicDAO sessionPersonBasicDAO;
	
	private SessionPersonValidator sessionPersonValidator;
	

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody SessionPerson sessionPerson, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String sessionGid = sessionPerson.getSessionGid();
		
		try {
			Session session = sessionBasicDAO.get(sessionGid);
			if(session == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return new AddResult("Session (" + sessionGid + ") not found.");
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new AddResult("Required authorities not found.");
		}
		
		List<SessionPerson> sessionPersons;
		try {
			sessionPersons = sessionPersonBasicDAO.getAllBySessionGid(sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		for(SessionPerson sp : sessionPersons) {
			if(sp.getPersonGid().equalsIgnoreCase(sessionPerson.getPersonGid())) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new AddResult("Already a member of the session team.");
			}
		}
		
		return doAdd(sessionPerson, request, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody SessionPerson sessionPerson, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		SessionPerson current;
		try {
			current = sessionPersonBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("SessionPerson (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, current.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new EditResult("Required authorities not found.");
		}
		
		sessionPerson.setGid(current.getGid());
		sessionPerson.setPersonGid(current.getPersonGid());
		
		return doEdit(sessionPerson, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		SessionPerson sessionPerson;
		try {
			sessionPerson = sessionPersonBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(sessionPerson == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("SessionPerson (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, sessionPerson.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}

		if(authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new RemoveResult("Required authorities not found.");
		}
		
		return doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		SessionPerson sessionPerson;
		try {
			sessionPerson = sessionPersonBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(sessionPerson == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return Collections.emptyMap();
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, sessionPerson.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			Session session;
			try {
				session = sessionBasicDAO.get(sessionPerson.getSessionGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				return Collections.emptyMap();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				return Collections.emptyMap();
			}
		}
		
		return sessionPerson;
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "session")
	public Object getAllBySessionGid(@RequestParam String user, @RequestParam("session") String sessionGid, HttpServletResponse response) throws Exception {
	
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			Session session;
			try {
				session = sessionBasicDAO.get(sessionGid);
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(session == null) {
				return Collections.emptyList();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				return Collections.emptyList();
			}
		}
	
		try {
			return sessionPersonBasicDAO.getAllBySessionGid(sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return SESSION_PERSON_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<SessionPerson> getModelBasicDAO() {
		return sessionPersonBasicDAO;
	}

	@Override
	public ModelValidator<SessionPerson> getModelValidator() {
		return sessionPersonValidator;
	}

	public SessionBasicDAO getSessionBasicDAO() {
		return sessionBasicDAO;
	}
	public void setSessionBasicDAO(SessionBasicDAO sessionBasicDAO) {
		this.sessionBasicDAO = sessionBasicDAO;
	}

	public SessionPersonBasicDAO getSessionPersonBasicDAO() {
		return sessionPersonBasicDAO;
	}
	public void setSessionPersonBasicDAO(SessionPersonBasicDAO sessionPersonBasicDAO) {
		this.sessionPersonBasicDAO = sessionPersonBasicDAO;
	}

	public SessionPersonValidator getSessionPersonValidator() {
		return sessionPersonValidator;
	}
	public void setSessionPersonValidator(SessionPersonValidator sessionPersonValidator) {
		this.sessionPersonValidator = sessionPersonValidator;
	}
}
