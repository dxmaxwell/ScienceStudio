/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupMember interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface LoginGroupMember extends Serializable {

	public int getId();
	public void setId(int id);
 
	public String getPersonUid();
	public void setPersonUid(String personUid);
	
	public int getLoginGroupId();
	public void setLoginGroupId(int loginGroupId);
}
