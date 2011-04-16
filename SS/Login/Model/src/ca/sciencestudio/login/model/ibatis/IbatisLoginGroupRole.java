/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupRole class.
 *     
 */
package ca.sciencestudio.login.model.ibatis;

import ca.sciencestudio.login.model.LoginGroupRole;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupRole implements LoginGroupRole {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int loginRoleId;
	private int loginGroupId;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getLoginRoleId() {
		return loginRoleId;
	}
	@Override
	public void setLoginRoleId(int loginRoleId) {
		this.loginRoleId = loginRoleId;
	}
	
	@Override
	public int getLoginGroupId() {
		return loginGroupId;
	}
	@Override
	public void setLoginGroupId(int loginGroupId) {
		this.loginGroupId = loginGroupId;
	}
}
