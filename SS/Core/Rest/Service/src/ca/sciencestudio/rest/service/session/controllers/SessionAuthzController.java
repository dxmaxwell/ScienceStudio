/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.ArrayList;
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
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.validators.SessionValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class SessionAuthzController extends AbstractSessionAuthzController<Session> {

	private static final String SESSION_MODEL_URL = "/sessions";

	private SessionValidator sessionValidator;
	
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Session session, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!facility.equals(getSessionBasicDAO().getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		
		// Check permissions. //
		return doAdd(session, request, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Session session, @PathVariable String gid, HttpServletResponse response) throws Exception {
		session.setGid(gid);
		
		// Check permissions. //
		return doEdit(session, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// Check permissions. //
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		Session session = getSessionBasicDAO().get(gid);
		if(session == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		
		if(!isSessionMember(user, gid) && !hasLoginRole(user, LOGIN_ROLE_ADMIN_PERSONS) && !isProjectMember(user, user, session.getProjectGid())) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyMap();
		}
		
		return session;
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Session> getAll(@RequestParam String user, HttpServletResponse response) {
		try {
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return getSessionBasicDAO().getAll();
			}
				
			List<Session> memberSessions = getSessionBasicDAO().getAllByPersonGid(user);
			
			List<Session> projectSessions = new ArrayList<Session>();
			List<ProjectPerson> projectPersons = getProjectPersons(user, user);
			for(ProjectPerson projectPerson : projectPersons) {
				projectSessions.addAll(getSessionBasicDAO().getAllByProjectGid(projectPerson.getProjectGid()));
			}
			
			// Combine both lists carefully to avoid duplicates. //
			List<Session> sessions = new ArrayList<Session>(projectSessions);
			for(Session memberSession : memberSessions) {
				boolean add = true;
				for(Session projectSession : projectSessions) {
					if(projectSession.getGid().equals(memberSession.getGid())) {
						add = false;
						break;
					}
				}
				if(add) {
					sessions.add(memberSession);
				}
			}
			
			return Collections.unmodifiableList(sessions);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.GET, params = "project")
	public Object getAllByProjectGid(@RequestParam String user, @RequestParam("project") String projectGid, HttpServletResponse response) throws Exception {
		try {
			if(isProjectMember(user, user, projectGid)) {
				return getSessionBasicDAO().getAllByProjectGid(projectGid);
			}
			
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return getSessionBasicDAO().getAllByProjectGid(projectGid);
			}
			
			List<Session> authzSessions = new ArrayList<Session>();
			List<SessionPerson> sessionPersons = getSessionPersons(user);
			List<Session> sessions = getSessionBasicDAO().getAllByProjectGid(projectGid);
			for(Session session : sessions) {
				for(SessionPerson sessionPerson : sessionPersons) {
					if(sessionPerson.getSessionGid().equals(session.getGid())) {
						authzSessions.add(session);
						break;
					}
				}
			}
				
			return Collections.unmodifiableList(authzSessions);	
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return SESSION_MODEL_URL;
	}
	
	@Override
	public ModelBasicDAO<Session> getModelBasicDAO() {
		return getSessionBasicDAO();
	}
	
	@Override
	public ModelValidator<Session> getModelValidator() {
		return sessionValidator;
	}

	public SessionValidator getSessionValidator() {
		return sessionValidator;
	}
	public void setSessionValidator(SessionValidator sessionValidator) {
		this.sessionValidator = sessionValidator;
	}
}
