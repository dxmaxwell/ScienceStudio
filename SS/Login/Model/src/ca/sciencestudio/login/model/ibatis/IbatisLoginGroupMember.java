/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisAuthorityMember class.
 *     
 */
package ca.sciencestudio.login.model.ibatis;

import ca.sciencestudio.login.model.LoginGroupMember;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupMember implements LoginGroupMember {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String personUid;
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
	public String getPersonUid() {
		return personUid;
	}
	@Override
	public void setPersonUid(String personUid) {
		this.personUid = personUid;
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
