/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AuthorityDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginRole;

/**
 * @author maxweld
 *
 */
public interface LoginRoleDAO {
	public LoginRole createLoginRole();
	
	public int addLoginRole(LoginRole loginRole);
	public void editLoginRole(LoginRole loginRole);
	public void removeLoginRole(LoginRole loginRole);
	public void removeLoginRole(int loginRoleId);
	
	public LoginRole getLoginRoleById(int loginRoleId);
	public LoginRole getLoginRoleByName(String name);
	
	public List<LoginRole> getLoginRoleList();
	public List<LoginRole> getLoginRoleListByPersonUid(String personUid);
}
