/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BeamlineSessionProxyEventListener class.
 *
 */
package ca.sciencestudio.vespers.device.proxy.event;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.util.state.SessionStateMap;
import ca.sciencestudio.util.state.support.SessionStateMapException;

import ca.sciencestudio.device.messaging.DeviceMessageSender;
import ca.sciencestudio.device.proxy.event.DeviceProxyEvent;
import ca.sciencestudio.device.proxy.event.ReadOnlyDeviceProxyEventListener;
import ca.sciencestudio.device.proxy.event.ReadWriteDeviceProxyEventListener;

/**
 * @author maxweld
 *
 */
public class BeamlineSessionProxyEventListener extends ReadOnlyDeviceProxyEventListener implements SessionStateMap {

	private static final String VALUE_KEY_PROJECT_ID = "projectId";
	private static final String VALUE_KEY_PROJECT_NAME = "projectName";
	private static final String VALUE_KEY_SESSION_ID = "sessionId";
	private static final String VALUE_KEY_SESSION_NAME = "sessionName";
	private static final String VALUE_KEY_SESSION_TIMEOUT = "sessionTimeout";
	private static final String VALUE_KEY_PROPOSAL = "proposal";
	private static final String VALUE_KEY_EXPERIMENT_ID = "experimentId";
	private static final String VALUE_KEY_EXPERIMENT_NAME = "experimentName";
	private static final String VALUE_KEY_SCAN_ID = "scanId";
	private static final String VALUE_KEY_SCAN_NAME = "scanName";
	private static final String VALUE_KEY_CONTROLLER_UID = "controllerUid";
	private static final String VALUE_KEY_CONTROLLER_NAME = "controllerName";

	private static final String VALUE_KEY_XRD_MODE = "xrdMode";
	private static final String VALUE_KEY_XRD_TRIGGER_MODE = "triggerMode";

	private static final int DEFAULT_PROJECT_ID = 0;
	private static final String DEFAULT_PROJECT_NAME = "NOT AVAILABLE";
	private static final int DEFAULT_SESSION_ID = 0;
	private static final String DEFAULT_SESSION_NAME = "NOT AVAILABLE";
	private static final int DEFAULT_SESSION_TIMEOUT = 0;
	private static final String DEFAULT_PROPOSAL = "NOT AVAILABLE";
	private static final int DEFAULT_EXPERIMENT_ID = 0;
	private static final String DEFAULT_EXPERIMENT_NAME = "NOT AVAILABLE";
	private static final int DEFAULT_SCAN_ID = 0;
	private static final String DEFAULT_SCAN_NAME = "NOT AVAILABLE";
	private static final String DEFAULT_CONTROLLER_UID = "";
	private static final String DEFAULT_CONTROLLER_NAME = "NOT AVAILABLE";

	protected static final int DEFAULT_RUNNING_SESSION_ID = 0;
	
	private static final String INITIAL_EXPIMENT_NAME = "SELECT AN EXPERIMENT";

	private ReadWriteDeviceProxyEventListener delegateDeviceProxy = new ReadWriteDeviceProxyEventListener();
	
	private ProjectDAO projectDAO;
	private SessionDAO sessionDAO;

	private String DEFAULT_XRD_MODE;
	private String DEFAULT_XRD_TRIGGER_MODE;
	
	public BeamlineSessionProxyEventListener() {
		super();
		setDefaultValues();
	}

	@Override
	public boolean isRunning() {
		return (DEFAULT_RUNNING_SESSION_ID < getRunningSessionId()); 
	}
	
	@Override
	public int getRunningSessionId() {
		Integer sessionId = (Integer) get(VALUE_KEY_SESSION_ID);
		if(sessionId instanceof Number) {
			return ((Number)sessionId).intValue();
		}
		else {
			return DEFAULT_RUNNING_SESSION_ID;
		}
	}

	@Override
	public void stopSession() throws SessionStateMapException {
		stopSession(getRunningSessionId());
	}
	
	@Override
	public void startSession(int sessionId) throws SessionStateMapException {
		
		if(isRunning()) {
			throw new SessionStateMapException("Laboratory Session is already running.");
		}
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			throw new SessionStateMapException("Unknown Laboratory Session ID. (1)");
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			throw new SessionStateMapException("Unknown Laboratory Session ID. (2)");
		}
		
		Date now = new Date();

		Date startDateTime = session.getStartDate();
		if(now.before(startDateTime)) {
			throw new SessionStateMapException("Laboratory Session is not currently scheduled.");
		}
		
		Date endDateTime = session.getEndDate();
		if(now.after(endDateTime)) {
			throw new SessionStateMapException("Laboratory Session is not currently scheduled.");
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		values.put("start", "");
		values.put("sessionId", session.getId());
		values.put("sessionEndTime", endDateTime.getTime());
		
		delegateDeviceProxy.putAll(values);
	}
	
	@Override
	public void stopSession(int sessionId) throws SessionStateMapException {
		
		if(!isRunning()) {
			throw new SessionStateMapException("Laboratory Session is already stopped.");
		}
		
		if(sessionId != getRunningSessionId()) {
			throw new SessionStateMapException("Laboratory Session is not running.");
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		values.put("stop", "");
		
		delegateDeviceProxy.putAll(values);
	}


	@Override
	public void handleEvent(DeviceProxyEvent deviceStateMapEvent) {

		Integer oldSessionId = (Integer) get(VALUE_KEY_SESSION_ID);
		super.handleEvent(deviceStateMapEvent);
		Integer newSessionId = (Integer) get(VALUE_KEY_SESSION_ID);

		if((newSessionId != null) && !newSessionId.equals(oldSessionId)) {

			setDefaultValues();

			Session session = sessionDAO.getSessionById(newSessionId);
			if(session == null) { return; }

			Project project = projectDAO.getProjectById(session.getProjectId());
			if(project == null) { return; }

			put(VALUE_KEY_PROJECT_ID, project.getId());
			put(VALUE_KEY_PROJECT_NAME, project.getName());
			put(VALUE_KEY_SESSION_ID, session.getId());
			put(VALUE_KEY_SESSION_NAME, session.getName());
			put(VALUE_KEY_PROPOSAL, session.getProposal());
			put(VALUE_KEY_EXPERIMENT_NAME, INITIAL_EXPIMENT_NAME);
		}
	}
	
	@Override
	public void setName(String name) {
		delegateDeviceProxy.setName(name);
		super.setName(name);
	}
	
	@Override
	public void setDeviceMessageSender(DeviceMessageSender deviceMessageSender) {
		delegateDeviceProxy.setDeviceMessageSender(deviceMessageSender);
		super.setDeviceMessageSender(deviceMessageSender);
	}
	
	public String getControllerUid() {
		Object controllerUid = get(VALUE_KEY_CONTROLLER_UID);
		if(controllerUid instanceof String) {
			return (String) controllerUid;
		} else {
			return DEFAULT_CONTROLLER_UID;
		}
	}
	
	public void setController(Person person) {
		put(VALUE_KEY_CONTROLLER_UID, person.getUid());
		put(VALUE_KEY_CONTROLLER_NAME, getPersonName(person));
	}
	
	protected void setRunningSessionId(int sessionId) {
		put(VALUE_KEY_SESSION_ID, sessionId);
	}
	
	protected void setDefaultValues() {
		put(VALUE_KEY_PROJECT_ID, DEFAULT_PROJECT_ID);
		put(VALUE_KEY_PROJECT_NAME, DEFAULT_PROJECT_NAME);
		put(VALUE_KEY_SESSION_ID, DEFAULT_SESSION_ID);
		put(VALUE_KEY_SESSION_NAME, DEFAULT_SESSION_NAME);
		put(VALUE_KEY_SESSION_TIMEOUT, DEFAULT_SESSION_TIMEOUT);
		put(VALUE_KEY_PROPOSAL, DEFAULT_PROPOSAL);
		put(VALUE_KEY_EXPERIMENT_ID, DEFAULT_EXPERIMENT_ID);
		put(VALUE_KEY_EXPERIMENT_NAME, DEFAULT_EXPERIMENT_NAME);
		put(VALUE_KEY_SCAN_ID, DEFAULT_SCAN_ID);
		put(VALUE_KEY_SCAN_NAME, DEFAULT_SCAN_NAME);
		put(VALUE_KEY_CONTROLLER_UID, DEFAULT_CONTROLLER_UID);
		put(VALUE_KEY_CONTROLLER_NAME, DEFAULT_CONTROLLER_NAME);
		put(VALUE_KEY_TIMESTAMP, new Date());
		put(VALUE_KEY_XRD_MODE, DEFAULT_XRD_MODE);
		put(VALUE_KEY_XRD_TRIGGER_MODE, DEFAULT_XRD_TRIGGER_MODE);
	}
	
	protected String getPersonName(Person person) {
		return person.getFirstName() + " " + person.getLastName();
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

	public void setDEFAULT_XRD_MODE(String defaultxrdmode) {
		DEFAULT_XRD_MODE = defaultxrdmode;
	}

	public void setDEFAULT_XRD_TRIGGER_MODE(String defaultxrdtriggermode) {
		DEFAULT_XRD_TRIGGER_MODE = defaultxrdtriggermode;
	}	
}
