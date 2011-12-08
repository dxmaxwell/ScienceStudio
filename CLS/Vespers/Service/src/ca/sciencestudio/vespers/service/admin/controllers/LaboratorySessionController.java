/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      LaboratorySessionController class.
 *      	     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.state.support.SessionStateMapException;
import ca.sciencestudio.util.web.ValidationBindingResult;
import ca.sciencestudio.vespers.service.admin.backers.LaboratorySessionBacker;
import ca.sciencestudio.vespers.device.proxy.event.BeamlineSessionProxyEventListener;

/**
 * @author maxweld
 *
 */
@Controller
public class LaboratorySessionController extends AbstractBeamlineAdminController {
	
	private static final int START_BEAMLINE_SESSION_POLLS = 10;
	private static final long START_BEAMLINE_SESSION_SLEEP = 3000;
	
	private static final String MODEL_KEY_MESSAGE = "message";
	private static final String MODEL_KEY_ERROR_MESSAGE = "errorMessage";
	private static final String MODEL_KEY_RUNNING_SESSION_GID = "runningSessionGid";
	private static final String MODEL_KEY_FACILITY_NAME = "facilityName";
	private static final String MODEL_KEY_LABORATORY_NAME = "laboratoryName";
	private static final String MODEL_KEY_LABORATORY_SESSION = "laboratorySession";
	private static final String MODEL_KEY_LABORATORY_SESSION_LIST = "laboratorySessionList";
	
	private static final String VIEW_SHOW = "frag/laboratorySession/show";
	private static final String VIEW_EDIT = "frag/laboratorySession/edit";
	private static final String VIEW_INDEX = "frag/laboratorySession/index";
	
	private static final String RUNNING_LABORATORY_SESSION_STATUS = "Running";
	private static final String STOPPED_LABORATORY_SESSION_STATUS = "Stopped";
	
	private static final String DEFAULT_SESSION_GID = "0";
	
	private String laboratoryName;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	private BeamlineSessionProxyEventListener beamlineSessionProxy;
	
	@RequestMapping(value = "/session/{sessionGid}/show.html", method = RequestMethod.GET)
	public String getSessionShow(@PathVariable String sessionGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session GID.");
			return VIEW_SHOW;
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project GID.");
			return VIEW_SHOW;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionGid));
		
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/session/{sessionGid}/edit.html", method = RequestMethod.GET)
	public String getSessionEdit(@PathVariable String sessionGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session GID.");
			return VIEW_EDIT;
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project GID.");
			return VIEW_EDIT;
		}
		
		LaboratorySessionBacker laboratorySession =  new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionGid));
		
		if(laboratorySession.getGid().equalsIgnoreCase(getRunningSessionGid())) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session is running. Cannot edit running session.");
			return VIEW_EDIT;
		}
		
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		return VIEW_EDIT;
	}
	
	@RequestMapping(value = "/session/{sessionGid}/edit.html", method = RequestMethod.POST)
	public String postSessionEdit(LaboratorySessionBacker laboratorySession, Errors errors, @PathVariable String sessionGid, ModelMap model) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		if((sessionGid == null) || sessionGid.equalsIgnoreCase(getRunningSessionGid())) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session is running.  Cannot update running session.");
			return VIEW_EDIT;
		}
		
		laboratorySession.setGid(sessionGid);
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = sessionAuthzDAO.edit(user, laboratorySession).get();
		if(result.hasErrors()) {
			BindingResult bindingResult = new ValidationBindingResult(laboratorySession, MODEL_KEY_LABORATORY_SESSION, LaboratorySessionBacker.transformResult(result));
			model.putAll(bindingResult.getModel());
			return VIEW_EDIT;
		}
		
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/session/{sessionGid}/start.html", method = RequestMethod.GET)
	public String getSessionStart(@PathVariable String sessionGid, HttpServletRequest request, ModelMap model) {
		
		if(!canAdminBeamline()) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Not authorized to administrate beamline.");
			return VIEW_SHOW;
		}
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session GID.");
			return VIEW_SHOW;
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Project not found. Invalid project GID.");
			return VIEW_SHOW;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionGid));
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		
		if(laboratorySession.getGid().equalsIgnoreCase(getRunningSessionGid())) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session is already running.");
			return VIEW_SHOW;
		}
				
		if(!getRunningSessionGid().equalsIgnoreCase(DEFAULT_SESSION_GID)) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Another Laboratory Session is already running.");
			return VIEW_SHOW;
		}
		
		try {
			beamlineSessionProxy.startSession(user, sessionGid);
		}
		catch(SessionStateMapException e) {
			model.put(MODEL_KEY_ERROR_MESSAGE, e.getMessage());
			return VIEW_SHOW;
		}
		
		boolean success = false;
		try {
			for(int poll=0; poll<START_BEAMLINE_SESSION_POLLS; poll++) {
				Thread.sleep(START_BEAMLINE_SESSION_SLEEP);
				if(laboratorySession.getGid().equalsIgnoreCase(getRunningSessionGid())) {
					success = true;
					break;
				}
			}
		}
		catch(InterruptedException e) {
			// nothing to do //
		}
		
		// Must refresh the value in the model set by @ModelAttribute. //
		model.put(MODEL_KEY_RUNNING_SESSION_GID, getRunningSessionGid());
		
		if(success) {
			laboratorySession.setStatus(getLaboratorySessionStatus(sessionGid));
			model.put(MODEL_KEY_MESSAGE, "Session Started Successfully.");
		}
		else {
			long wait = (START_BEAMLINE_SESSION_POLLS * START_BEAMLINE_SESSION_SLEEP) / 1000; 
			model.put(MODEL_KEY_ERROR_MESSAGE, "Session Failed to Start in " + wait + " seconds.");
		}
		
		return VIEW_SHOW;
	}
	
	@RequestMapping(value = "/session/{sessionGid}/stop.html", method = RequestMethod.GET)
	public String getSessionStop(@PathVariable String sessionGid, HttpServletRequest request, ModelMap model) {
		
		if(!canAdminBeamline()) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Not authorized to administrate beamline.");
			return VIEW_SHOW;
		}
		
		String laboratoryGid = getLaboratoryGid();
		if(laboratoryGid == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Facility or Laboratory not found.");
			return VIEW_SHOW;
		}
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid session GID.");
			return VIEW_SHOW;
		}
		
		if(!session.getLaboratoryGid().equalsIgnoreCase(laboratoryGid)) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Session is not for the specified Laboratory.");
			return VIEW_SHOW;
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null ) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "Laboratory Session not found. Invalid project GID.");
			return VIEW_SHOW;
		}
		
		LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
		laboratorySession.setStatus(getLaboratorySessionStatus(sessionGid));
		model.put(MODEL_KEY_LABORATORY_SESSION, laboratorySession);
		
		try {
			beamlineSessionProxy.stopSession(sessionGid);
		}
		catch(SessionStateMapException e) {
			model.put(MODEL_KEY_ERROR_MESSAGE, e.getMessage());
			return VIEW_SHOW; 
		}
		
		boolean success = false;
		try {
			for(int poll=0; poll<START_BEAMLINE_SESSION_POLLS; poll++) {
				Thread.sleep(START_BEAMLINE_SESSION_SLEEP);
				if(getRunningSessionGid().equalsIgnoreCase(DEFAULT_SESSION_GID)) {
					success = true;
					break;
				}
			}
		}
		catch(InterruptedException e) {}
		
		// Must refresh the value in the model set by @ModelAttribute. //
		model.put(MODEL_KEY_RUNNING_SESSION_GID, getRunningSessionGid());
		
		if(success) {
			laboratorySession.setStatus(getLaboratorySessionStatus(sessionGid));
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
	
	@ModelAttribute(MODEL_KEY_RUNNING_SESSION_GID)
	public String getRunningSessionGid() {
		return beamlineSessionProxy.getRunningSessionGid();
	}
	
	protected String getLaboratoryGid() {
		
		Facility facility = facilityAuthzDAO.get(this.facility).get();
		if(facility == null) {
			return null;
		}
		
		List<Laboratory> laboratoryList = laboratoryAuthzDAO.getAllByFacilityGid(facility.getGid()).get();
		
		for(Laboratory laboratory : laboratoryList) {
			if(laboratory.getName().equalsIgnoreCase(laboratoryName)) {
				return laboratory.getGid();
			}
		}
		
		return null;
	}
	
	protected List<LaboratorySessionBacker> getLaboratorySessionList() {
		
		String laboratoryGid = getLaboratoryGid();
		if(laboratoryGid == null) {
			return Collections.emptyList();
		}
		
		String user = SecurityUtil.getPersonGid();
		
		List<Session> sessionList = sessionAuthzDAO.getAll(user).get();
		
		List<Session> filteredSessionList = new ArrayList<Session>();
		
		for(Session session : sessionList) {
			if(session.getLaboratoryGid().equalsIgnoreCase(laboratoryGid)) {
				filteredSessionList.add(session);
			}
		}
		
		Map<String,Data<Project>> dataProjectMap = new HashMap<String,Data<Project>>();
		
		for(Session session : filteredSessionList) {
			if(!dataProjectMap.containsKey(session.getProjectGid())) {
				dataProjectMap.put(session.getProjectGid(), projectAuthzDAO.get(user, session.getProjectGid()));
			}
		}
		
		List<LaboratorySessionBacker> laboratorySessionList = new ArrayList<LaboratorySessionBacker>();
		
		for(Session session : filteredSessionList) {
			Data<Project> dataProject = dataProjectMap.get(session.getProjectGid());
			if(dataProject != null) {
				Project project = dataProject.get();
				if(project != null) {
					LaboratorySessionBacker laboratorySession = new LaboratorySessionBacker(project, session);
					laboratorySession.setStatus(getLaboratorySessionStatus(session.getGid()));
					laboratorySessionList.add(laboratorySession);
				}
			}
		}
		
		return laboratorySessionList;
	}

	protected String getLaboratorySessionStatus(String sessionGid) {
		if(beamlineSessionProxy.getRunningSessionGid().equalsIgnoreCase(sessionGid)) {
			return RUNNING_LABORATORY_SESSION_STATUS;
		} else {
			return STOPPED_LABORATORY_SESSION_STATUS;
		}
	}

	@ModelAttribute(MODEL_KEY_FACILITY_NAME)
	public String getFacilityName() {
		return facility;
	}

	@ModelAttribute(MODEL_KEY_LABORATORY_NAME)
	public String getLaboratoryName() {
		return laboratoryName;
	}
	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public LaboratoryAuthzDAO getLaboratoryAuthzDAO() {
		return laboratoryAuthzDAO;
	}
	public void setLaboratoryAuthzDAO(LaboratoryAuthzDAO laboratoryAuthzDAO) {
		this.laboratoryAuthzDAO = laboratoryAuthzDAO;
	}

	public BeamlineSessionProxyEventListener getBeamlineSessionProxy() {
		return beamlineSessionProxy;
	}
	public void setBeamlineSessionProxy(BeamlineSessionProxyEventListener beamlineSessionProxy) {
		this.beamlineSessionProxy = beamlineSessionProxy;
	}
}
