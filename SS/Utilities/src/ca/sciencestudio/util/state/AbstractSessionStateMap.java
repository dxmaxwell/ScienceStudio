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

	protected static final String STATE_KEY_RUNNING_SESSION_ID = "sessionId";
	
	protected static final int DEFAULT_RUNNING_SESSION_ID = 0;
	
	@Override
	public boolean isRunning() {
		return (DEFAULT_RUNNING_SESSION_ID < getRunningSessionId()); 
	}
	
	@Override
	public int getRunningSessionId() {
		Serializable sessionId = get(STATE_KEY_RUNNING_SESSION_ID);
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

	protected void setRunningSessionId(int sessionId) {
		put(STATE_KEY_RUNNING_SESSION_ID, sessionId);
	}
	
	protected void setDefaultValues() {
		setRunningSessionId(DEFAULT_RUNNING_SESSION_ID);
	}
}
