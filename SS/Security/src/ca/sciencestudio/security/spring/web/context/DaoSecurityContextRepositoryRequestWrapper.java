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

/**
 * @author maxweld
 *
 */
public class DaoSecurityContextRepositoryRequestWrapper extends HttpServletRequestWrapper {

	private String loginSessionId = "";
	private boolean newSecurityContext = false;
	private boolean loginSessionIdFromParameter = false;
	
	public DaoSecurityContextRepositoryRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String getLoginSessionId() {
		return loginSessionId;
	}
	public void setLoginSessionId(String loginSessionId) {
		this.loginSessionId = loginSessionId;
	}

	public boolean isNewSecurityContext() {
		return newSecurityContext;
	}
	public void setNewSecurityContext(boolean newSecurityContext) {
		this.newSecurityContext = newSecurityContext;
	}

	public boolean isLoginSessionIdFromParameter() {
		return loginSessionIdFromParameter;
	}
	public void setLoginSessionIdFromParameter(boolean loginSessionIdFromParameter) {
		this.loginSessionIdFromParameter = loginSessionIdFromParameter;
	}
}
