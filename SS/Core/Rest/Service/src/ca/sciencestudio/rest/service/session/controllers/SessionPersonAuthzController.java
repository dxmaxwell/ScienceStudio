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

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.validators.SessionPersonValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class SessionPersonAuthzController extends AbstractSessionAuthzController<SessionPerson> {

	private static final String SESSION_PERSON_MODEL_PATH = "/session/persons";

	private SessionPersonValidator sessionPersonValidator;
	
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		return new Permissions(true);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody SessionPerson sp, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!facility.equals(getSessionPersonBasicDAO().getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		// Check permissions. //
		return doAdd(sp, request, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody SessionPerson sp, @PathVariable String gid, HttpServletResponse response) throws Exception {
		sp.setGid(gid);
		
		// Check permissions. //
		return doEdit(sp, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// Check permissions. //
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		try {
			SessionPerson sessionPerson = getSessionPersonBasicDAO().get(gid);
			if(sessionPerson == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return Collections.emptyMap();
			}
			
			if(isSessionMember(user, sessionPerson.getSessionGid())) {
				return sessionPerson;
			}
			
			Session session = getSessionBasicDAO().get(sessionPerson.getSessionGid());
			if((session != null) && isProjectMember(user, user, session.getProjectGid())) {
				return sessionPerson;
			}
					
			if(!hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return sessionPerson;
			}
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyMap();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}

	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "session")
	public List<SessionPerson> getAll(@RequestParam String user, @RequestParam("session") String sessionGid, HttpServletResponse response) {
		try {
			if(isSessionMember(user, sessionGid)) {
				return getSessionPersonBasicDAO().getAllBySessionGid(sessionGid);
			}
			
			Session session = getSessionBasicDAO().get(sessionGid);
			if((session != null) && isProjectMember(user, user, session.getProjectGid())) {
				return getSessionPersonBasicDAO().getAllBySessionGid(sessionGid);
			}
			
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return getSessionPersonBasicDAO().getAllBySessionGid(sessionGid);	
			}
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyList();
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
		return getSessionPersonBasicDAO();
	}

	@Override
	public ModelValidator<SessionPerson> getModelValidator() {
		return sessionPersonValidator;
	}

	public SessionPersonValidator getSessionPersonValidator() {
		return sessionPersonValidator;
	}
	public void setSessionPersonValidator(SessionPersonValidator sessionPersonValidator) {
		this.sessionPersonValidator = sessionPersonValidator;
	}
}
