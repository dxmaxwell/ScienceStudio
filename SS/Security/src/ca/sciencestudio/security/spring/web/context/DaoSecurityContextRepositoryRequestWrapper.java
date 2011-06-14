/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DaoSecurityContextRepositoryRequestWrapper class.
 *     
 */
package ca.sciencestudio.security.spring.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import ca.sciencestudio.login.model.LoginSession;

/**
 * @author maxweld
 *
 */
public class DaoSecurityContextRepositoryRequestWrapper extends HttpServletRequestWrapper {

	private LoginSession loginSession = null;
	private boolean loginSessionUuidFromParameter = false;
	
	public DaoSecurityContextRepositoryRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public boolean isNewLoginSesion() {
		return (loginSession == null);
	}
	
	public LoginSession getLoginSession() {
		return loginSession;
	}
	public void setLoginSession(LoginSession loginSession) {
		this.loginSession = loginSession;
	}

	public boolean isLoginSessionUuidFromParameter() {
		return loginSessionUuidFromParameter;
	}
	public void setLoginSessionUuidFromParameter(boolean loginSessionUuidFromParameter) {
		this.loginSessionUuidFromParameter = loginSessionUuidFromParameter;
	}
}
