/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginGroup;

/**
 * @author maxweld
 *
 */
public interface LoginGroupDAO {
	public LoginGroup createLoginGroup();
	
	public int addLoginGroup(LoginGroup loginGroup);
	public void editLoginGroup(LoginGroup loginGroup);
	public void removeLoginGroup(LoginGroup loginGroup);
	public void removeLoginGroup(int loginGroupId);
	
	public LoginGroup getLoginGroupById(int loginGroupId);
	
	public List<LoginGroup> getLoginGroupList();
	public List<LoginGroup> getLoginGroupListByPersonUid(String personUid);
}
