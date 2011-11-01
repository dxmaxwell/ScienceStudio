/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityAuthorityAccessor class.
 *     
 */
package ca.sciencestudio.security.authz.accessors;

import java.util.List;

import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.login.model.dao.LoginRoleDAO;
import ca.sciencestudio.security.authz.AccessorAuthorites;

/**
 * @author maxweld
 * 
 *
 */
public class FacilityAuthorityAccessor {

	private LoginRoleDAO loginRoleDAO;
 
	public AccessorAuthorites getAuthorities(String user) {
		
		AccessorAuthorites authorities = new AccessorAuthorites();
		
		List<LoginRole> loginRoles = loginRoleDAO.getAllByPersonGid(user);
		for(LoginRole loginRole : loginRoles) {
			authorities.addFacilityAuthority(loginRole);
		}
		
		return authorities;
	}
	
	public AccessorAuthorites getAuthorities(String user, String gid) {
		return getAuthorities(user);
	}
	
	public LoginRoleDAO getLoginRoleDAO() {
		return loginRoleDAO;
	}
	public void setLoginRoleDAO(LoginRoleDAO loginRoleDAO) {
		this.loginRoleDAO = loginRoleDAO;
	}
}
