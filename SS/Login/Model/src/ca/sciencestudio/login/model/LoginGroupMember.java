/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupMember class.
 *     
 */
package ca.sciencestudio.login.model;

import ca.sciencestudio.login.model.Model;

/**
 * @author maxweld
 *
 */
public final class LoginGroupMember implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_LOGIN_GROUP_ID = 0;
	public static final String DEFAULT_PERSON_GID = "";
	
	private int id = DEFAULT_ID;
	private int LoginGroupId = DEFAULT_LOGIN_GROUP_ID;
	private String personGid = DEFAULT_PERSON_GID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getLoginGroupId() {
		return LoginGroupId;
	}
	public void setLoginGroupId(int loginGroupId) {
		LoginGroupId = loginGroupId;
	}
	
	public String getPersonGid() {
		return personGid;
	}
	public void setPersonGid(String personGid) {
		this.personGid = personGid;
	}
}
