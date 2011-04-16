/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      LaboratorySessionController class.
 *      	     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.util.state.support.SessionStateMapException;
import ca.sciencestudio.vespers.service.admin.backers.LaboratorySessionBacker;
import ca.sciencestudio.vespers.service.admin.validators.LaboratorySessionValidator;
import ca.sciencestudio.vespers.device.proxy.event.BeamlineSessionProxyEventListener;

/**
 * @author maxweld
 *
 */
@Controller
public class LaboratorySessionController {
	
	private static final int START_BEAMLINE_SESSION_POLLS = 10;
	private static final long START_BEAMLINE_SESSION_SLEEP = 3000;
	
	private static final String MODEL_KEY_MESSAGE = "message";
	private static final String MODEL_KEY_ERROR_MESSAGE = "errorMessage";
	private static final String MODEL_KEY_RUNNING_SESSION_ID = "runningSessionId";
	private static final String MODEL_KEY_FACILITY_NAME = "facilityName";
	private static final String MODEL_KEY_LABORATORY_NAME = "laboratoryName";
	private static final String MODEL_KEY_LABORATORY_SESSION = "laboratorySession";
	private static final String MODEL_KEY_LABORATORY_SESSION_LIST = "laboratorySessionList";
	
	private static final String VIEW_SHOW = "frag/laboratorySession/show";
	private static final String VIEW_EDIT = "frag/laboratorySession/edit";
	private static final String VIEW_INDEX = "frag/laboratorySession/index";
	
	private static final String RUNNING_LABORATORY_SESSION_STATUS = "Running";
	private static final String STOPPED_LABORATORY_SESSION_STATUS = "Stopped";
	
	private String facilityName;
	private String laboratoryName;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private LaboratorySessionValidator laboratorySessionValidator;
	
	private BeamlineSessionProxyEventListener beamlineSessionProxy;
	
	@RequestMapping(value = "/session/{sessionId}/show.html", method = RequestMethod.GET)
	public String getSessionShow(@PathVariable int sessionId, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session ID.");
			return VIEW_SHOW;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project ID.");
			return VIEW_SHOW;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
		
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/session/{sessionId}/edit.html", method = RequestMethod.GET)
	public String getSessionEdit(@PathVariable int sessionId, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session ID.");
			return VIEW_EDIT;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project ID.");
			return VIEW_EDIT;
		}
		
		LaboratorySessionBacker laboratorySession =  new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
		
		if(laboratorySession.getSessionId() == getRunningSessionId()) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session is running. Cannot edit running session.");
			return VIEW_EDIT;
		}
		
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		return VIEW_EDIT;
	}
	
	@RequestMapping(value = "/session/{sessionId}/edit.html", method = RequestMethod.POST)
	public String postSessionEdit(@PathVariable int sessionId, HttpServletRequest request, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session ID.");
			return VIEW_EDIT;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project ID.");
			return VIEW_EDIT;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
			
		if(laboratorySession.getSessionId() == getRunningSessionId()) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session is running.  Cannot update running session.");
			return VIEW_EDIT;
		}
		
		BindException errors = BindAndValidateUtils.bindAndValidate(laboratorySession, MODEL_KEY_LABORATORY_SESSION, request, laboratorySessionValidator);
		
		if(errors.hasErrors()) {
			model.putAll(errors.getModel());
			return VIEW_EDIT;
		}
		
		// Ensure session and project IDs are not modified. //
		laboratorySession.setSessionId(sessionId);
		laboratorySession.setProjectId(project.getId());
		/////////////////////////////////////////////////////
		
		session = laboratorySession.createSession(sessionDAO);
		sessionDAO.editSession(session);
		
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/session/{sessionId}/start.html", method = RequestMethod.GET)
	public String getSessionStart(@PathVariable int sessionId, HttpServletRequest request, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session ID.");
			return VIEW_SHOW;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project ID.");
			return VIEW_SHOW;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		
		if(laboratorySession.getSessionId() == getRunningSessionId()) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session is already running.");
			return VIEW_SHOW;
		}
		
		if(getRunningSessionId() != 0) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session cannot start, another is running.");
			return VIEW_SHOW;
		}
		
		try {
			beamlineSessionProxy.startSession(sessionId);
		}
		catch(SessionStateMapException e) {
			model.put(MODEL_KEY_ERROR_MESSAGE, e.getMessage());
			return VIEW_SHOW;
		}
		
		boolean success = false;
		try {
			for(int poll=0; poll<START_BEAMLINE_SESSION_POLLS; poll++) {
				Thread.sleep(START_BEAMLINE_SESSION_SLEEP);
				if(laboratorySession.getSessionId() == getRunningSessionId()) {
					success = true;
					break;
				}
			}
		}
		catch(InterruptedException e) {
			// nothing to do //
		}
		
		// Must refresh the value in the model set by @ModelAttribute. //
		model.put(MODEL_KEY_RUNNING_SESSION_ID, getRunningSessionId());
		
		if(success) {
			laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
			model.put(MODEL_KEY_MESSAGE, "Session Started Successfully.");
		}
		else {
			long wait = (START_BEAMLINE_SESSION_POLLS * START_BEAMLINE_SESSION_SLEEP) / 1000; 
			model.put(MODEL_KEY_ERROR_MESSAGE, "Session Failed to Start in " + wait + " seconds.");
		}
		
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/session/{sessionId}/stop.html", method = RequestMethod.GET)
	public String getSessionStop(@PathVariable int sessionId, HttpServletRequest request, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session ID.");
			return VIEW_SHOW;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project ID.");
			return VIEW_SHOW;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		
		try {
			beamlineSessionProxy.stopSession(sessionId);
		}
		catch(SessionStateMapException e) {
			model.put(MODEL_KEY_ERROR_MESSAGE, e.getMessage());
			return VIEW_SHOW; 
		}
		
		boolean success = false;
		try {
			for(int poll=0; poll<START_BEAMLINE_SESSION_POLLS; poll++) {
				Thread.sleep(START_BEAMLINE_SESSION_SLEEP);
				if(getRunningSessionId() == 0) {
					success = true;
					break;
				}
			}
		}
		catch(InterruptedException e) {}
		
		// Must refresh the value in the model set by @ModelAttribute. //
		model.put(MODEL_KEY_RUNNING_SESSION_ID, getRunningSessionId());
		
		if(success) {
			laboratorySession.setStatus(getLaboratorySessionStatus(sessionId));
			model.put(MODEL_KEY_MESSAGE, "Session Stopped Successfully.");
		}
		else {
			long wait = (START_BEAMLINE_SESSION_POLLS * START_BEAMLINE_SESSION_SLEEP) / 1000; 
			model.put(MODEL_KEY_ERROR_MESSAGE, "Session Failed to Stop in " + wait + " seconds.");
		}
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/sessions.html", method = RequestMethod.GET)
	public String getSessionIndex(HttpServletRequest request, ModelMap model) {

		List<LaboratorySessionBacker> laboratorySessionList = getLaboratorySessionList();
		model.put(MODEL_KEY_LABORATORY_SESSION_LIST, laboratorySessionList);
		
		return VIEW_INDEX;
	}
	
	@ModelAttribute(MODEL_KEY_RUNNING_SESSION_ID)
	public int getRunningSessionId() {
		return beamlineSessionProxy.getRunningSessionId();
	}
	
	protected List<LaboratorySessionBacker> getLaboratorySessionList() {
		List<LaboratorySessionBacker> laboratorySessionList = new ArrayList<LaboratorySessionBacker>();
		
		List<Project> projectList = projectDAO.getProjectList();
		List<Session> sessionList = sessionDAO.getSessionListByLaboratoryNameAndFacilityName(laboratoryName, facilityName);
		
		for(Session session : sessionList) {
			Project project = null;
			for(Project p : projectList) {
				if(p.getId() == session.getProjectId()) {
					project = p;
					break;
				}
			}
			
			if(project != null) {
				LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
				laboratorySession.setStatus(getLaboratorySessionStatus(session.getId()));
				laboratorySessionList.add(laboratorySession);
			}
		}
		
		return laboratorySessionList;
	}

	protected String getLaboratorySessionStatus(int sessionId) {
		if(sessionId == beamlineSessionProxy.getRunningSessionId()) {
			return RUNNING_LABORATORY_SESSION_STATUS;
		}
		else {
			return STOPPED_LABORATORY_SESSION_STATUS;
		}
	}

	@ModelAttribute(MODEL_KEY_FACILITY_NAME)
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	@ModelAttribute(MODEL_KEY_LABORATORY_NAME)
	public String getLaboratoryName() {
		return laboratoryName;
	}
	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}
	
	public LaboratorySessionValidator getLaboratorySessionValidator() {
		return laboratorySessionValidator;
	}
	public void setLaboratorySessionValidator(LaboratorySessionValidator laboratorySessionValidator) {
		this.laboratorySessionValidator = laboratorySessionValidator;
	}

	public BeamlineSessionProxyEventListener getBeamlineSessionProxy() {
		return beamlineSessionProxy;
	}
	public void setBeamlineSessionProxy(BeamlineSessionProxyEventListener beamlineSessionProxy) {
		this.beamlineSessionProxy = beamlineSessionProxy;
	}
}
