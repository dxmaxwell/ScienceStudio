/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     NanofabSessionStateMap class.
 *     
 */
package ca.sciencestudio.nanofab.state;

import java.util.Date;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.util.net.TunnelManager;
import ca.sciencestudio.util.state.AbstractSessionStateMap;
import ca.sciencestudio.util.state.support.SessionStateMapException;

/**
 * @author maxweld
 *
 */
public class NanofabSessionStateMap extends AbstractSessionStateMap {
	
	protected static final String STATE_KEY_PROJECT_GID = "projectGid";
	protected static final String STATE_KEY_PROJECT_NAME = "projectName";
	protected static final String STATE_KEY_SESSION_GID = "sessionGid";
	protected static final String STATE_KEY_SESSION_NAME = "sessionName";
	protected static final String STATE_KEY_SESSION_TIMEOUT = "sessionTimeout";
	protected static final String STATE_KEY_SESSION_END_TIME = "sessionEndTime";
	protected static final String STATE_KEY_EXPERIMENT_GID = "experimentGid";
	protected static final String STATE_KEY_EXPERIMENT_NAME = "experimentName";
	protected static final String STATE_KEY_CONTROLLER_GID = "controllerGid";
	protected static final String STATE_KEY_CONTROLLER_NAME = "controllerName";
	protected static final String STATE_KEY_ADMINISTRATOR_GID = "administratorGid";
	
	protected static final String DEFAULT_PROJECT_GID = "0";
	protected static final String DEFAULT_PROJECT_NAME = "NOT AVAILABLE";
	protected static final String DEFAULT_SESSION_GID = "0";
	protected static final String DEFAULT_SESSION_NAME = "NOT AVAILABLE";
	protected static final long DEFAULT_SESSION_TIMEOUT = 0L;
	protected static final long DEFAULT_SESSION_END_TIME = 0L;
	protected static final String DEFAULT_EXPERIMENT_GID = "0";
	protected static final String DEFAULT_EXPERIMENT_NAME = "SELECT AN EXPERIMENT";
	protected static final String DEFAULT_CONTROLLER_GID = "0";
	protected static final String DEFAULT_CONTROLLER_NAME = "NOT AVAILABLE";
	protected static final String DEFAULT_ADMINISTRATOR_GID = "0";
	
	private static final long SESSION_THREAD_SLEEP_TIME = 1000L; 
	
	private Thread sessionThread;
	
	private SessionAuthzDAO sessionAuthzDAO;
	private ProjectAuthzDAO projectAuthzDAO;
	private TunnelManager tunnelManager;
	
	public NanofabSessionStateMap() {
		setDefaultValues();
	}
	
	
	public void startSession(String user, String sessionGid) throws SessionStateMapException {
		
		if(isRunning()) {
			throw new SessionStateMapException("Session is already running.");
		}
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			throw new SessionStateMapException("Session not found. Invalid Session GID.");
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			throw new SessionStateMapException("Project not found. Invalid Project GID.");
		}
		
		Date now = new Date();

		Date startDateTime = session.getStartDate();
		if(now.before(startDateTime)) {
			throw new SessionStateMapException("Session is not currently scheduled.");
		}
		
		Date endDateTime = session.getEndDate();
		if(now.after(endDateTime)) {
			throw new SessionStateMapException("Session is not currently scheduled.");
		}
		
		sessionThread = new NanofabSessionThread();
		
		setProject(project);
		setSession(session);
		updateSessionTimeout();
		setAdministratorGid(user);
		setRunningSessionGid(sessionGid);
	}

	@Override
	public void stopSession(String sessionGid) throws SessionStateMapException {
		
		if(!isRunning()) {
			throw new SessionStateMapException("Session is already stopped.");
		}
		
		if((sessionGid != null) && !sessionGid.equalsIgnoreCase(getRunningSessionGid())) {
			throw new SessionStateMapException("Session is not running.");
		}
		
		if(sessionThread != null) {
			sessionThread.interrupt();
		}
		
		doStopSession();
	}

	protected void doStopSession() throws SessionStateMapException {
		setDefaultValues();
		tunnelManager.closeAndRemoveAll();
		put(STATE_KEY_RUNNING_SESSION_GID, DEFAULT_RUNNING_SESSION_GID);
	}
	
	public String getProjectGid() {
		return (String) get(STATE_KEY_PROJECT_GID);
	}
	protected void setProjectGid(String projectGid) {
		put(STATE_KEY_PROJECT_GID, projectGid);
	}
	
	public String getProjectName() {
		return (String) get(STATE_KEY_PROJECT_NAME);
	}
	protected void setProjectName(String projectName) {
		put(STATE_KEY_PROJECT_NAME, projectName);
	}
	
	protected void setProject(Project project) {
		if(project != null) {
			setProjectGid(project.getGid());
			setProjectName(project.getName());
		}
	}
	
	public String getSessionGid() {
		return (String) get(STATE_KEY_SESSION_GID);
	}
	protected void setSessionGid(String sessionGid) {
		put(STATE_KEY_SESSION_GID, sessionGid);
	}
	
	public String getSessionName() {
		return (String) get(STATE_KEY_SESSION_NAME);
	}
	protected void setSessionName(String sessionName) {
		put(STATE_KEY_SESSION_NAME, sessionName);
	}
	
	public long getSessionTimeout() {
		return (Long) get(STATE_KEY_SESSION_TIMEOUT);
	}
	protected void updateSessionTimeout() {
		long sessionTimeout = (getSessionEndTime() - (new Date().getTime())) / 1000L;
		put(STATE_KEY_SESSION_TIMEOUT, Math.max(DEFAULT_SESSION_TIMEOUT, sessionTimeout));
	}
	
	public long getSessionEndTime() {
		return (Long) get(STATE_KEY_SESSION_END_TIME);
	}
	protected void setSessionEndTime(long endTime) {
		put(STATE_KEY_SESSION_END_TIME, endTime);
	}
	
	protected void setSession(Session session) {
		if(session != null) {
			setSessionGid(session.getGid());
			setSessionName(session.getName());
			setSessionEndTime(session.getEndDate().getTime());
		}
	}
	
	public String getExperimentGid() {
		return (String) get(STATE_KEY_EXPERIMENT_GID);
	}
	protected void setExperimentGid(String experimentGid) {
		put(STATE_KEY_EXPERIMENT_GID, experimentGid);
	}
	
	public String getExperimentName() {
		return (String) get(STATE_KEY_EXPERIMENT_NAME);
	}
	protected void setExperimentName(String experimentName) {
		put(STATE_KEY_EXPERIMENT_NAME, experimentName);
	}
	
	public void setExperiment(Experiment experiment) {
		setExperimentGid(experiment.getGid());
		setExperimentName(experiment.getName());
	}
	
	public String getControllerGid() {
		return (String) get(STATE_KEY_CONTROLLER_GID);
	}
	protected void setControllerGid(String controllerGid) {
		put(STATE_KEY_CONTROLLER_GID, controllerGid);
	}
	
	public String getControllerName() {
		return (String) get(STATE_KEY_CONTROLLER_NAME);
	}
	protected void setControllerName(String controllerName) {
		put(STATE_KEY_CONTROLLER_NAME, controllerName);
	}
	
	public void setController(Person person) {
		if(person != null) {
			setControllerGid(person.getGid());
			setControllerName(Person.getFullName(person));
		}
	}

	public void setControllerIfNotSet(Person person) {
		String controllerGid = getControllerGid();
		if((controllerGid == null) || controllerGid.equalsIgnoreCase(DEFAULT_CONTROLLER_GID)) {
			setController(person);
		}
	}
	
	public String getAdministratorGid() {
		return (String) get(STATE_KEY_ADMINISTRATOR_GID);
	}
	public void setAdministratorGid(String administratorGid) {
		put(STATE_KEY_ADMINISTRATOR_GID, administratorGid);
	}
	
	protected void setDefaultValues() {
		super.setDefaultValues();
		setAdministratorGid(DEFAULT_ADMINISTRATOR_GID);
		setControllerGid(DEFAULT_CONTROLLER_GID);
		setControllerName(DEFAULT_CONTROLLER_NAME);
		setProjectGid(DEFAULT_PROJECT_GID);
		setProjectName(DEFAULT_PROJECT_NAME);
		setSessionGid(DEFAULT_SESSION_GID);
		setSessionName(DEFAULT_SESSION_NAME);
		setExperimentGid(DEFAULT_EXPERIMENT_GID);
		setExperimentName(DEFAULT_EXPERIMENT_NAME);
		setSessionEndTime(DEFAULT_SESSION_END_TIME);
		updateSessionTimeout();
		setTimestamp(new Date());
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public void setTunnelManager(TunnelManager tunnelManager) {
		this.tunnelManager = tunnelManager;
	}

	protected class NanofabSessionThread extends Thread {
		
		public NanofabSessionThread() {
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			try {
				while(getSessionTimeout() > 0L) {
					sleep(SESSION_THREAD_SLEEP_TIME);
					updateSessionTimeout();
				}	
			}
			catch(InterruptedException e) {
				// Nothing to do. //
			}
			
			try {
				doStopSession();
			}
			catch(SessionStateMapException e) {
				logger.warn("Exception while stopping session: " + e.getMessage());
			}
		}
	}
}
