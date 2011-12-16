/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractSessionStateMap class.
 *     
 */
package ca.sciencestudio.util.state;

import java.io.Serializable;

import ca.sciencestudio.util.state.support.SessionStateMapException;

/**
 * @author maxweld
 *
 */
public abstract class AbstractSessionStateMap extends SimpleStateMap implements SessionStateMap {

	protected static final String STATE_KEY_RUNNING_SESSION_GID = "sessionGid";
	
	protected static final String DEFAULT_RUNNING_SESSION_GID = "0";
	
	@Override
	public boolean isRunning() {
		return (!DEFAULT_RUNNING_SESSION_GID.equalsIgnoreCase(getRunningSessionGid())); 
	}
	
	@Override
	public String getRunningSessionGid() {
		Serializable sessionGid = get(STATE_KEY_RUNNING_SESSION_GID);
		if(sessionGid instanceof String) {
			return (String)sessionGid;
		}
		else if(sessionGid instanceof Object) {
			return sessionGid.toString();
		}
		else {
			return DEFAULT_RUNNING_SESSION_GID;
		}
	}
	
	@Override
	public void stopSession() throws SessionStateMapException {
		stopSession(getRunningSessionGid());
	}

	protected void setRunningSessionGid(String sessionGid) {
		put(STATE_KEY_RUNNING_SESSION_GID, sessionGid);
	}
	
	protected void setDefaultValues() {
		setRunningSessionGid(DEFAULT_RUNNING_SESSION_GID);
	}
}
