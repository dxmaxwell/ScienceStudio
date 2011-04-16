/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupRole interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface LoginGroupRole extends Serializable {

	public int getId();
	public void setId(int loginGroupRoleId);
	
	public int getLoginRoleId();
	public void setLoginRoleId(int loginRoleId);
	
	public int getLoginGroupId();
	public void setLoginGroupId(int loginGroupId);
}
