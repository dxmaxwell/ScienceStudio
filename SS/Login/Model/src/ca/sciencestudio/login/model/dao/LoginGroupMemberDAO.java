/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupMemberDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginGroupMember;

/**
 * @author maxweld
 *
 */
public interface LoginGroupMemberDAO {
	public LoginGroupMember createLoginGroupMember();
	
	public int addLoginGroupMember(LoginGroupMember loginGroupMember);
	public void editLoginGroupMember(LoginGroupMember loginGroupMember);
	public void removeLoginGroupMember(int loginGroupMemberId);
	public void removeLoginGroupMember(LoginGroupMember loginGroupMember);
	
	public LoginGroupMember getLoginGroupMemberById(int loginGroupMemberId);
	
	public List<LoginGroupMember> getLoginGroupMemberList();
}
