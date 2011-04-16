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
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.util.net.TunnelManager;
import ca.sciencestudio.util.state.AbstractSessionStateMap;
import ca.sciencestudio.util.state.support.SessionStateMapException;

/**
 * @author maxweld
 *
 */
public class NanofabSessionStateMap extends AbstractSessionStateMap {
	
	protected static final String STATE_KEY_PROJECT_ID = "projectId";
	protected static final String STATE_KEY_PROJECT_NAME = "projectName";
	protected static final String STATE_KEY_SESSION_ID = "sessionId";
	protected static final String STATE_KEY_SESSION_NAME = "sessionName";
	protected static final String STATE_KEY_SESSION_TIMEOUT = "sessionTimeout";
	protected static final String STATE_KEY_SESSION_END_TIME = "sessionEndTime";
	protected static final String STATE_KEY_EXPERIMENT_ID = "experimentId";
	protected static final String STATE_KEY_EXPERIMENT_NAME = "experimentName";
	protected static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	protected static final String STATE_KEY_CONTROLLER_NAME = "controllerName";
	
	protected static final int DEFAULT_PROJECT_ID = 0;
	protected static final String DEFAULT_PROJECT_NAME = "NOT AVAILABLE";
	protected static final int DEFAULT_SESSION_ID = 0;
	protected static final String DEFAULT_SESSION_NAME = "NOT AVAILABLE";
	protected static final long DEFAULT_SESSION_TIMEOUT = 0L;
	protected static final long DEFAULT_SESSION_END_TIME = 0L;
	protected static final int DEFAULT_EXPERIMENT_ID = 0;
	protected static final String DEFAULT_EXPERIMENT_NAME = "SELECT AN EXPERIMENT";
	protected static final String DEFAULT_CONTROLLER_UID = "";
	protected static final String DEFAULT_CONTROLLER_NAME = "NOT AVAILABLE";
	
	private static final long SESSION_THREAD_SLEEP_TIME = 1000L; 
	
	private Thread sessionThread;
	
	private SessionDAO sessionDAO;
	private ProjectDAO projectDAO;
	private TunnelManager tunnelManager;
	
	public NanofabSessionStateMap() {
		setDefaultValues();
	}
	
	public void startSession(int sessionId) throws SessionStateMapException {
		
		if(isRunning()) {
			throw new SessionStateMapException("Session is already running.");
		}
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			throw new SessionStateMapException("Session not found. Invalid Session ID.");
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			throw new SessionStateMapException("Project not found. Invalid Project ID.");
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
		setRunningSessionId(sessionId);
	}

	public void stopSession(int sessionId) throws SessionStateMapException {
		
		if(!isRunning()) {
			throw new SessionStateMapException("Session is already stopped.");
		}
		
		if(sessionId != getRunningSessionId()) {
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
		put(STATE_KEY_RUNNING_SESSION_ID, DEFAULT_RUNNING_SESSION_ID);
	}
	
	public int getProjectId() {
		return (Integer) get(STATE_KEY_PROJECT_ID);
	}
	protected void setProjectId(int projectId) {
		put(STATE_KEY_PROJECT_ID, projectId);
	}
	
	public String getProjectName() {
		return (String) get(STATE_KEY_PROJECT_NAME);
	}
	protected void setProjectName(String projectName) {
		put(STATE_KEY_PROJECT_NAME, projectName);
	}
	
	protected void setProject(Project project) {
		if(project != null) {
			setProjectId(project.getId());
			setProjectName(project.getName());
		}
	}
	
	public int getSessionId() {
		return (Integer) get(STATE_KEY_SESSION_ID);
	}
	protected void setSessionId(int sessionId) {
		put(STATE_KEY_SESSION_ID, sessionId);
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
			setSessionId(session.getId());
			setSessionName(session.getName());
			setSessionEndTime(session.getEndDate().getTime());
		}
	}
	
	public int getExperimentId() {
		return (Integer) get(STATE_KEY_EXPERIMENT_ID);
	}
	protected void setExperimentId(int experimentId) {
		put(STATE_KEY_EXPERIMENT_ID, experimentId);
	}
	
	public String getExperimentName() {
		return (String) get(STATE_KEY_EXPERIMENT_NAME);
	}
	protected void setExperimentName(String experimentName) {
		put(STATE_KEY_EXPERIMENT_NAME, experimentName);
	}
	
	public void setExperiment(Experiment experiment) {
		setExperimentId(experiment.getId());
		setExperimentName(experiment.getName());
	}
	
	public String getControllerUid() {
		return (String) get(STATE_KEY_CONTROLLER_UID);
	}
	protected void setControllerUid(String controllerUid) {
		put(STATE_KEY_CONTROLLER_UID, controllerUid);
	}
	
	public String getControllerName() {
		return (String) get(STATE_KEY_CONTROLLER_NAME);
	}
	protected void setControllerName(String controllerName) {
		put(STATE_KEY_CONTROLLER_NAME, controllerName);
	}
	
	public void setController(Person person) {		
		if(person != null) {
			setControllerUid(person.getUid());
			setControllerName(person.getFirstName() + " " + person.getLastName());
		}
	}

	protected void setDefaultValues() {
		super.setDefaultValues();
		setControllerUid(DEFAULT_CONTROLLER_UID);
		setControllerName(DEFAULT_CONTROLLER_NAME);
		setProjectId(DEFAULT_PROJECT_ID);
		setProjectName(DEFAULT_PROJECT_NAME);
		setSessionId(DEFAULT_SESSION_ID);
		setSessionName(DEFAULT_SESSION_NAME);
		setExperimentId(DEFAULT_EXPERIMENT_ID);
		setExperimentName(DEFAULT_EXPERIMENT_NAME);
		setSessionEndTime(DEFAULT_SESSION_END_TIME);
		updateSessionTimeout();
		setTimestamp(new Date());
	}
	
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}
	
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
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
