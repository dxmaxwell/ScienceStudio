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

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
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

	private static final String VALUE_KEY_STOP = "stop";
	private static final String VALUE_KEY_START = "start";
	private static final String VALUE_KEY_PROJECT_GID = "projectGid";
	private static final String VALUE_KEY_PROJECT_NAME = "projectName";
	private static final String VALUE_KEY_SESSION_GID = "sessionGid";
	private static final String VALUE_KEY_SESSION_NAME = "sessionName";
	private static final String VALUE_KEY_SESSION_TIMEOUT = "sessionTimeout";
	private static final String VALUE_KEY_SESSION_END_TIME = "sessionEndTime";
	private static final String VALUE_KEY_PROPOSAL = "proposal";
	private static final String VALUE_KEY_EXPERIMENT_GID = "experimentGid";
	private static final String VALUE_KEY_EXPERIMENT_NAME = "experimentName";
	private static final String VALUE_KEY_SCAN_GID = "scanGid";
	private static final String VALUE_KEY_SCAN_NAME = "scanName";
	private static final String VALUE_KEY_CONTROLLER_GID = "controllerGid";
	private static final String VALUE_KEY_CONTROLLER_NAME = "controllerName";
	private static final String VALUE_KEY_ADMINISTRATOR_GID = "administratorGid";
	private static final String VALUE_KEY_XRD_MODE = "xrdMode";
	private static final String VALUE_KEY_TECHNIQUE = "technique"; 
	private static final String VALUE_KEY_TECHNIQUE_CHANGE = "techniqueChanged";

	private static final String DEFAULT_PROJECT_GID = "0";
	private static final String DEFAULT_PROJECT_NAME = "NOT AVAILABLE";
	private static final String DEFAULT_SESSION_GID = "0";
	private static final String DEFAULT_SESSION_NAME = "NOT AVAILABLE";
	private static final int DEFAULT_SESSION_TIMEOUT = 0;
	private static final long DEFAULT_SESSION_END_TIME = 0;
	private static final String DEFAULT_PROPOSAL = "NOT AVAILABLE";
	private static final String DEFAULT_EXPERIMENT_GID = "0";
	private static final String DEFAULT_EXPERIMENT_NAME = "NOT AVAILABLE";
	private static final String DEFAULT_SCAN_GID = "0";
	private static final String DEFAULT_SCAN_NAME = "NOT AVAILABLE";
	private static final String DEFAULT_CONTROLLER_GID = "0";
	private static final String DEFAULT_CONTROLLER_NAME = "NOT AVAILABLE";

	private static final String DEFAULT_XRD_MODE = "init";
	private static final String DEFAULT_TECHNIQUE = "TBD"; 
	private static final String DEFAULT_TECHNIQUE_CHANGE = "No";

	protected static final String DEFAULT_ADMINISTRATOR_GID = "0";
	
	protected static final String DEFAULT_RUNNING_SESSION_GID = "0";
	
	private static final String INITIAL_EXPERIMENT_NAME = "SELECT AN EXPERIMENT";

	private ReadWriteDeviceProxyEventListener delegateDeviceProxy = new ReadWriteDeviceProxyEventListener();
	
	private ProjectAuthzDAO projectAuthzDAO;
	private SessionAuthzDAO sessionAuthzDAO;

	public BeamlineSessionProxyEventListener() {
		super();
		setDefaultValues();
	}

	@Override
	public boolean isRunning() {
		return (!DEFAULT_RUNNING_SESSION_GID.equalsIgnoreCase(getRunningSessionGid()));
	}
	
	@Override
	public String getRunningSessionGid() {
		Object sessionGid = get(VALUE_KEY_SESSION_GID);
		if(sessionGid != null) {
			return sessionGid.toString();
		} else {
			return DEFAULT_RUNNING_SESSION_GID;
		}
	}

	@Override
	public void stopSession() throws SessionStateMapException {
		stopSession(getRunningSessionGid());
	}
	
	@Override
	public void startSession(String user, String sessionGid) throws SessionStateMapException {
		
		if(isRunning()) {
			throw new SessionStateMapException("Laboratory Session is already running.");
		}
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			throw new SessionStateMapException("Unknown Laboratory Session GID. (1)");
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			throw new SessionStateMapException("Unknown Laboratory Session GID. (2)");
		}
		
		Date now = new Date();

		Date startDate = session.getStartDate();
		if(now.before(startDate)) {
			throw new SessionStateMapException("Laboratory Session is not currently scheduled.");
		}
		
		Date endDate = session.getEndDate();
		if(now.after(endDate)) {
			throw new SessionStateMapException("Laboratory Session is not currently scheduled.");
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		values.put(VALUE_KEY_START, true);
		values.put(VALUE_KEY_SESSION_GID, session.getGid());
		values.put(VALUE_KEY_SESSION_END_TIME, endDate.getTime());
		values.put(VALUE_KEY_ADMINISTRATOR_GID, user);
		delegateDeviceProxy.putAll(values);
	}
	
	@Override
	public void stopSession(String sessionGid) throws SessionStateMapException {
		
		if(!isRunning()) {
			throw new SessionStateMapException("Laboratory Session is already stopped.");
		}
		
		if(!getRunningSessionGid().equalsIgnoreCase(sessionGid)) {
			throw new SessionStateMapException("Laboratory Session is not running.");
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		values.put(VALUE_KEY_STOP, true);
		delegateDeviceProxy.putAll(values);
	}


	@Override
	public void handleEvent(DeviceProxyEvent deviceProxyEvent) {
		if(!getDeviceId().equals(deviceProxyEvent.getDeviceId())) {
			return;
		}
		
		String currentGid = (String) get(VALUE_KEY_SESSION_GID);
		super.handleEvent(deviceProxyEvent);
		
		String sessionGid = (String) get(VALUE_KEY_SESSION_GID);
		if(sessionGid == null) {
			return;
		}
			
		if(!sessionGid.equalsIgnoreCase(currentGid)) {
			
			if(!sessionGid.equalsIgnoreCase(DEFAULT_SESSION_GID)) {
				// Session has started. //
				String administratorGid = (String) get(VALUE_KEY_ADMINISTRATOR_GID);
				if(administratorGid == null) {
					logger.warn("No administrator GID found in BeamlineSessionProxy, cannot start session.");
					return;
				}
				
				setDefaultValues();
				
				Session session = sessionAuthzDAO.get(administratorGid, sessionGid).get();
				if(session == null) {
					logger.warn("Session (" + sessionGid + ") not found or not authorized, cannot start session.");
					return;
				}
	
				Project project = projectAuthzDAO.get(administratorGid, session.getProjectGid()).get();
				if(project == null) {
					logger.warn("Project (" + session.getProjectGid() + ") not found or not authorized, cannot start session.");
					return;
				}
	
				put(VALUE_KEY_PROJECT_GID, project.getGid());
				put(VALUE_KEY_PROJECT_NAME, project.getName());
				put(VALUE_KEY_SESSION_GID, session.getGid());
				put(VALUE_KEY_SESSION_NAME, session.getName());
				put(VALUE_KEY_PROPOSAL, session.getProposal());
				put(VALUE_KEY_ADMINISTRATOR_GID, administratorGid);
				put(VALUE_KEY_EXPERIMENT_NAME, INITIAL_EXPERIMENT_NAME);
			}
			else {
				// Session has stopped. //
				setDefaultValues();
			}
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
	
	public String getControllerGid() {
		Object controllerGid = get(VALUE_KEY_CONTROLLER_GID);
		if(controllerGid != null) {
			return controllerGid.toString();
		} else {
			return DEFAULT_CONTROLLER_GID;
		}
	}
	
	public void setController(Person person) {
		put(VALUE_KEY_CONTROLLER_GID, person.getGid());
		put(VALUE_KEY_CONTROLLER_NAME, Person.getFullName(person));
	}
	
	public void setControllerIfNotSet(Person person) {
		String controllerGid = getControllerGid();
		if((controllerGid == null) || controllerGid.equalsIgnoreCase(DEFAULT_CONTROLLER_GID)) {
			setController(person);
		}
	}
	
	protected void setDefaultValues() {
		put(VALUE_KEY_PROJECT_GID, DEFAULT_PROJECT_GID);
		put(VALUE_KEY_PROJECT_NAME, DEFAULT_PROJECT_NAME);
		put(VALUE_KEY_SESSION_GID, DEFAULT_SESSION_GID);
		put(VALUE_KEY_SESSION_NAME, DEFAULT_SESSION_NAME);
		put(VALUE_KEY_SESSION_TIMEOUT, DEFAULT_SESSION_TIMEOUT);
		put(VALUE_KEY_SESSION_END_TIME, DEFAULT_SESSION_END_TIME);
		put(VALUE_KEY_PROPOSAL, DEFAULT_PROPOSAL);
		put(VALUE_KEY_EXPERIMENT_GID, DEFAULT_EXPERIMENT_GID);
		put(VALUE_KEY_EXPERIMENT_NAME, DEFAULT_EXPERIMENT_NAME);
		put(VALUE_KEY_SCAN_GID, DEFAULT_SCAN_GID);
		put(VALUE_KEY_SCAN_NAME, DEFAULT_SCAN_NAME);
		put(VALUE_KEY_CONTROLLER_GID, DEFAULT_CONTROLLER_GID);
		put(VALUE_KEY_CONTROLLER_NAME, DEFAULT_CONTROLLER_NAME);
		put(VALUE_KEY_ADMINISTRATOR_GID, DEFAULT_ADMINISTRATOR_GID);
		put(VALUE_KEY_TIMESTAMP, new Date());
		put(VALUE_KEY_XRD_MODE, DEFAULT_XRD_MODE);
		put(VALUE_KEY_TECHNIQUE, DEFAULT_TECHNIQUE);
		put(VALUE_KEY_TECHNIQUE_CHANGE, DEFAULT_TECHNIQUE_CHANGE);
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

}
