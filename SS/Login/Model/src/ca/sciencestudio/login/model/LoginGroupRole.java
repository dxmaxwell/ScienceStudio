/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupRole class.
 *     
 */
package ca.sciencestudio.login.model;

import ca.sciencestudio.login.model.Model;

/**
 * @author maxweld
 *
 */
public final class LoginGroupRole implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_LOGIN_ROLE_ID = 0;
	public static final int DEFAULT_LOGIN_GROUP_ID = 0;
	
	private int id = DEFAULT_ID;
	private int loginRoleId = DEFAULT_LOGIN_ROLE_ID;
	private int loginGroupId = DEFAULT_LOGIN_GROUP_ID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getLoginRoleId() {
		return loginRoleId;
	}
	public void setLoginRoleId(int loginRoleId) {
		this.loginRoleId = loginRoleId;
	}
	
	public int getLoginGroupId() {
		return loginGroupId;
	}
	public void setLoginGroupId(int loginGroupId) {
		this.loginGroupId = loginGroupId;
	}
}
