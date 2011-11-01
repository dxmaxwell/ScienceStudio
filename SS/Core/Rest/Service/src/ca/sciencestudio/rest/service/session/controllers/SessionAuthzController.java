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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;
import ca.sciencestudio.model.session.validators.SessionValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.AuthorizationException;
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
public class SessionAuthzController extends AbstractSessionAuthzController<Session> {

	private static final String SESSION_MODEL_URL = "/sessions";

	private LaboratoryBasicDAO laboratoryBasicDAO;
	
	private ExperimentBasicDAO experimentBasicDAO;
	
	private SessionBasicDAO sessionBasicDAO;
	
	private SessionPersonBasicDAO sessionPersonBasicDAO;
	
	private SessionValidator sessionValidator;
	
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Session session, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String laboratoryGid = session.getLaboratoryGid();
		
		try {
			Laboratory laboratory = laboratoryBasicDAO.get(laboratoryGid);
			if(laboratory == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return new AddResult("Laboratory (" + laboratoryGid + ") not found.");
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new AddResult("Required authorities not found.");
		}
		
		Project project;
		try {
			project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		}
		catch(AuthorizationException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new AddResult("Unauthorized to access project.");
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(project == null) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new AddResult("Project session references not found.");
		}
		
		return doAdd(session, request, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Session session, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Session current;
		try {
			current = sessionBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("Session (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(authorities.containsNone(SESSION_EXPERIMENTER, FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new EditResult("Required authorities not found.");
		}
		
		session.setGid(current.getGid());
		session.setProjectGid(current.getProjectGid());
		session.setLaboratoryGid(current.getLaboratoryGid());
		
		if(authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			if(!current.getProposal().equals(session.getProposal())) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new EditResult("Not allowed to edit proposal.");
			}
			
			if(!current.getStartDate().equals(session.getStartDate())) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new EditResult("Not allowed to edit start date.");
			}
			
			if(!current.getEndDate().equals(session.getEndDate())) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new EditResult("Not allowed to edit end dates.");
			}
		}
		
		return doEdit(session, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@PathVariable String gid, @RequestParam String user, HttpServletResponse response) throws Exception {
		
		Session session;
		try {
			session = sessionBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(session == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("Session (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new RemoveResult("Required authorities not found.");
		}
		
		List<Experiment> experiments;
		try {
			experiments = experimentBasicDAO.getAllBySessionGid(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!experiments.isEmpty()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new RemoveResult("Session has associated Experiments.");
		}
		
		try {
			List<SessionPerson> sessionPersons = sessionPersonBasicDAO.getAllBySessionGid(gid);
			for(SessionPerson sessionPerson : sessionPersons) {
				sessionBasicDAO.remove(sessionPerson.getGid());
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		return doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Session session;
		try {
			session = sessionBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
			
		if(session == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
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
		
		return session;
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.GET)
	public Object getAll(@RequestParam String user, HttpServletResponse response) {
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		List<Session> sessions;
		try {
			sessions = sessionBasicDAO.getAll();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(authorities.contains(FACILITY_ADMIN_SESSIONS)) {
			return sessions;
		}
				
		Set<String> sessionGids = new HashSet<String>();
		
		try {
			List<Session> memberSessions = sessionBasicDAO.getAllByPersonGid(user);
			for(Session session : memberSessions) {
				sessionGids.add(session.getGid());
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		try {	
			List<Project> projects = projectAuthzDAO.getAll(user).get();
			for(Project project : projects) {
				List<Session> memberSessions = sessionBasicDAO.getAllByProjectGid(project.getGid());
				for(Session session : memberSessions) {
					sessionGids.add(session.getGid());
				}
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		List<Session> memberSessions = new ArrayList<Session>();
		for(String sessionGid: sessionGids) {
			for(Session session : sessions) {
				if(session.getGid().equalsIgnoreCase(sessionGid)) {
					memberSessions.add(session);
				}
			}
		}
		
		return Collections.unmodifiableList(memberSessions);
	}

	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.GET, params = "project")
	public Object getAllByProjectGid(@RequestParam String user, @RequestParam("project") String projectGid, HttpServletResponse response) throws Exception {
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		List<Session> sessions;
		try {
			sessions = sessionBasicDAO.getAllByProjectGid(projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(authorities.contains(FACILITY_ADMIN_SESSIONS)) {
			return sessions;
		}
		
		try {
			authorities = projectAuthzDAO.getAuthorities(user, projectGid).get();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(authorities.containsProjectAuthority() || authorities.contains(FACILITY_ADMIN_PROJECTS)) {
			return sessions;
		}
		
		List<Session> memberSessions;
		try {
			memberSessions = sessionBasicDAO.getAllByPersonGid(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		List<Session> finalSessions = new ArrayList<Session>();
		for(Session m : memberSessions) {
			for(Session s : sessions) {
				if(m.getGid().equalsIgnoreCase(s.getGid())) {
					finalSessions.add(s);
					break;
				}
			}
		}
		
		return Collections.unmodifiableList(finalSessions);
	}
	
	@Override
	public String getModelPath() {
		return SESSION_MODEL_URL;
	}
	
	@Override
	public ModelBasicDAO<Session> getModelBasicDAO() {
		return sessionBasicDAO;
	}
	
	@Override
	public ModelValidator<Session> getModelValidator() {
		return sessionValidator;
	}

	public LaboratoryBasicDAO getLaboratoryBasicDAO() {
		return laboratoryBasicDAO;
	}
	public void setLaboratoryBasicDAO(LaboratoryBasicDAO laboratoryBasicDAO) {
		this.laboratoryBasicDAO = laboratoryBasicDAO;
	}

	public ExperimentBasicDAO getExperimentBasicDAO() {
		return experimentBasicDAO;
	}
	public void setExperimentBasicDAO(ExperimentBasicDAO experimentBasicDAO) {
		this.experimentBasicDAO = experimentBasicDAO;
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

	public SessionValidator getSessionValidator() {
		return sessionValidator;
	}
	public void setSessionValidator(SessionValidator sessionValidator) {
		this.sessionValidator = sessionValidator;
	}
}
