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
	public String getRunningSessionGid();
	
	public void startSession(String user, String sessionGid) throws SessionStateMapException;
	public void stopSession(String sessionGid) throws SessionStateMapException;
	public void stopSession() throws SessionStateMapException;
}
