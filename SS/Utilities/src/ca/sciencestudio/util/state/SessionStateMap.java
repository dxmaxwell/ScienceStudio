/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionStateMap interface.
 *     
 */
package ca.sciencestudio.util.state;

import ca.sciencestudio.util.state.support.SessionStateMapException;

/**
 * @author maxweld
 *
 */
public interface SessionStateMap extends StateMap {
	
	public boolean isRunning();
	public int getRunningSessionId();
	
	public void startSession(int sessionId) throws SessionStateMapException;
	public void stopSession(int sessionId) throws SessionStateMapException;
	public void stopSession() throws SessionStateMapException;
}
