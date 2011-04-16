/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginHistoryDAO implementation.
 *     
 *     Depreciated, but useful for future development.
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.LoginHistory;
import ca.sciencestudio.login.model.dao.LoginHistoryDAO;

/**
 * @author maxweld
 *
 */
public class IbatisLoginHistoryDAO extends SqlMapClientDaoSupport implements LoginHistoryDAO {

	public LoginHistory getLoginHistoryById(int loginHistoryId) {	
		Object obj = getSqlMapClientTemplate().queryForObject("getLoginHistoryById", loginHistoryId);
		logger.debug("Get LoginHistory with id: " + loginHistoryId);
		return (LoginHistory) obj;
	}
	
	public int addLoginHistory(LoginHistory loginHistory) {
		Integer id = (Integer) getSqlMapClientTemplate().insert("addLoginHistory", loginHistory);
		logger.debug("Add new LoginHistory with id: " + id);
		return id;
	}

	public void editLoginHistory(LoginHistory loginHistory) {
		int rowsAffected = getSqlMapClientTemplate().update("editLoginHistory", loginHistory);
		logger.debug("Edit LoginHistory with id: " + loginHistory.getId() + ", rows affected: " + rowsAffected);
	}

	public void removeLoginHistory(int loginHistoryId) {
		int rowsAffected = getSqlMapClientTemplate().delete("removeLoginHistory", loginHistoryId);
		logger.debug("Remove LoginHistory with id: " + loginHistoryId + ", rows affected: " + rowsAffected);
	}
	
	@SuppressWarnings("unchecked")
	public List<LoginHistory> getLoginHistoryList() {
		List<?> objList = getSqlMapClientTemplate().queryForList("getLoginHistoryList");
		logger.debug("Get LoginHistory list, size: " + objList.size());
		return (List<LoginHistory>)objList;
	}	
}
