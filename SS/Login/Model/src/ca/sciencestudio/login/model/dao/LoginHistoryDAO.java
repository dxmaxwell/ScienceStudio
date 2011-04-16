/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginHistoryDAO interface.
 *     
 *     Depreciated, but useful for future development.
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginHistory;

/**
 * @author maxweld
 *
 */
public interface LoginHistoryDAO {

	public LoginHistory getLoginHistoryById(int loginHistoryId);
	public int addLoginHistory(LoginHistory loginHistory);
	public void editLoginHistory(LoginHistory loginHistory);
	public void removeLoginHistory(int loginHistoryId);
	public List<LoginHistory> getLoginHistoryList();
}
