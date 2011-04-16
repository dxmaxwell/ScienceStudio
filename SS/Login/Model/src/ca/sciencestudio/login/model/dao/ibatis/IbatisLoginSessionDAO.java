/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginSessionDao implementation.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.LoginSession;
import ca.sciencestudio.login.model.dao.LoginSessionDAO;

/**
 * @author maxweld
 *  
 */
public class IbatisLoginSessionDAO extends SqlMapClientDaoSupport implements LoginSessionDAO {

	private SqlMapClientTemplate sqlMapClient = getSqlMapClientTemplate();
	
	public String addLoginSession(LoginSession loginSession) {
		sqlMapClient.insert("addLoginSession", loginSession);
		if(logger.isTraceEnabled()) {
			logger.trace("Add new LoginSession with id: " + loginSession.getId());
		}
		return loginSession.getId();
	}

	public void editLoginSession(LoginSession loginSession) {
		sqlMapClient.update("editLoginSession", loginSession);
		if(logger.isTraceEnabled()) {
			logger.trace("Edit LoginSession with id: " + loginSession.getId());
		}
	}

	public LoginSession getLoginSessionById(String loginSessionId) {
		LoginSession loginSession = (LoginSession) sqlMapClient.queryForObject("getLoginSessionById", loginSessionId);
		if(loginSession != null && logger.isTraceEnabled()) {
			logger.trace("Get loginSession with id: " + loginSession.getId());
		}
		return loginSession;
	}

	public void removeLoginSession(String loginSessionId) {
		sqlMapClient.delete("removeLoginSession", loginSessionId);
		if(logger.isTraceEnabled()) {
			logger.trace("Remove LoginSession with id: " + loginSessionId);
		}
	}

	@SuppressWarnings("unchecked")
	public List<LoginSession> getLoginSessionList() {
		List<?> list = sqlMapClient.queryForList("getLoginSessionList");
		if(logger.isTraceEnabled()) {
			logger.trace("Get LoginSession list, size: " + list.size());
		}
		return (List<LoginSession>) list;
	}
}
