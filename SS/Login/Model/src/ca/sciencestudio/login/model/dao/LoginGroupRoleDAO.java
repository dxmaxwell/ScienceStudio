/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupRoleRoleDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginGroupRole;

/**
 * @author maxweld
 *
 */
public interface LoginGroupRoleDAO {
	public LoginGroupRole createLoginGroupRole();
	
	public int addLoginGroupRole(LoginGroupRole loginGroupRole);
	public void editLoginGroupRole(LoginGroupRole loginGroupRole);
	public void removeLoginGroupRole(LoginGroupRole loginGroupRole);
	public void removeLoginGroupRole(int loginGroupRoleId);
	
	public LoginGroupRole getLoginGroupRoleById(int loginGroupRoleId);
	
	public List<LoginGroupRole> getLoginGroupRoleList();	
}
