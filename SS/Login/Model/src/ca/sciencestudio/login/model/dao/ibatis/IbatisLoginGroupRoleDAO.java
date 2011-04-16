/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupRoleDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.LoginGroupRole;
import ca.sciencestudio.login.model.dao.LoginGroupRoleDAO;
import ca.sciencestudio.login.model.ibatis.IbatisLoginGroupRole;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupRoleDAO extends SqlMapClientDaoSupport implements LoginGroupRoleDAO {

	@Override
	public LoginGroupRole createLoginGroupRole() {
		return new IbatisLoginGroupRole();
	}

	@Override
	public int addLoginGroupRole(LoginGroupRole loginGroupRole) {
		int key = (Integer) getSqlMapClientTemplate().insert("addLoginGroupRole", loginGroupRole);
		logger.info("Added new login group role with id: " + key);
		return key;
	}

	@Override
	public void editLoginGroupRole(LoginGroupRole loginGroupRole) {
		int count = getSqlMapClientTemplate().update("editLoginGroupRole", loginGroupRole);
		logger.info("Edited login group role id: " + loginGroupRole.getId() + ", rows affected: " + count);
	}

	@Override
	public void removeLoginGroupRole(int loginGroupRoleId) {
		int count = getSqlMapClientTemplate().delete("removeLoginGroupRole", loginGroupRoleId);
		logger.info("Removed login group role id: " + loginGroupRoleId + ", rows affected: " + count);
	}
	
	@Override
	public void removeLoginGroupRole(LoginGroupRole loginGroupRole) {
		int count = getSqlMapClientTemplate().delete("removeLoginGroupRole", loginGroupRole.getId());
		logger.info("Removed login group role id: " + loginGroupRole.getId() + ", rows affected: " + count);
	}

	@Override
	public LoginGroupRole getLoginGroupRoleById(int loginGroupRoleId) {
		LoginGroupRole loginGroupRole = (LoginGroupRole) getSqlMapClientTemplate().queryForObject("getLoginGroupRoleById", loginGroupRoleId);
		logger.info("Get login group role: " + loginGroupRoleId);
		return loginGroupRole;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LoginGroupRole> getLoginGroupRoleList() {
		List<LoginGroupRole> list = getSqlMapClientTemplate().queryForList("getLoginGroupRoleList");
		logger.info("Get login group role list, size: " + list.size());
		return list;
	}

}
