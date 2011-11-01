/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelAuthzController abstract class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.List;
import java.util.ArrayList;

import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.login.model.dao.LoginRoleDAO;
import ca.sciencestudio.model.Model;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractModelAuthzController<T extends Model> extends AbstractModelController<T> {

	protected static final String LOGIN_ROLE_ADMIN_PERSONS = "ADMIN_PERSONS";
	protected static final String LOGIN_ROLE_ADMIN_PROJECTS = "ADMIN_PROJECTS";
	protected static final String LOGIN_ROLE_ADMIN_SESSIONS = "ADMIN_SESSIONS";
	protected static final String LOGIN_ROLE_ADMIN_FACILITY = "ADMIN_FACILITY";
	
	private LoginRoleDAO loginRoleDAO;
	
	protected List<LoginRole> getLoginRoles(String personGid) {
		return loginRoleDAO.getAllByPersonGid(personGid);	
	}
	
	protected List<String> getLoginRoleNames(String personGid) {
		List<LoginRole> loginRoles = getLoginRoles(personGid);
		List<String> loginRoleNames = new ArrayList<String>(loginRoles.size());
		for(LoginRole loginRole : loginRoles) {
			loginRoleNames.add(loginRole.getName());
		}
		return loginRoleNames;
	}
	
	protected boolean hasLoginRole(String personGid, String role) {
		List<LoginRole> loginRoles = getLoginRoles(personGid);
		for(LoginRole loginRole : loginRoles) {
			if(loginRole.getName().equals(role)) {
				return true;
			}
		}
		return false;
	}
	
	public LoginRoleDAO getLoginRoleDAO() {
		return loginRoleDAO;
	}
	public void setLoginRoleDAO(LoginRoleDAO loginRoleDAO) {
		this.loginRoleDAO = loginRoleDAO;
	}	
}
