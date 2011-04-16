/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginSessionDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginSession;

/**
 * @author maxweld
 *  
 */
public interface LoginSessionDAO {

	public LoginSession getLoginSessionById(String loginSessionId);
	public String addLoginSession(LoginSession loginSession);
	public void editLoginSession(LoginSession loginSession);
	public void removeLoginSession(String loginSessionId);
	public List<LoginSession> getLoginSessionList();
}
