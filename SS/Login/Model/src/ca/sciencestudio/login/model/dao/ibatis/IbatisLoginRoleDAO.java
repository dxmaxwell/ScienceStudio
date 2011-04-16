/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginRoleDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.login.model.dao.LoginRoleDAO;
import ca.sciencestudio.login.model.ibatis.IbatisLoginRole;

/**
 * @author maxweld
 *
 */
public class IbatisLoginRoleDAO extends SqlMapClientDaoSupport implements LoginRoleDAO {

	@Override
	public LoginRole createLoginRole() {
		return new IbatisLoginRole();
	}

	@Override
	public int addLoginRole(LoginRole loginRole) {
		int key = (Integer) getSqlMapClientTemplate().insert("addLoginRole", loginRole);
		logger.info("Added new LoginRole with id: " + key);
		return key;
	}

	@Override
	public void editLoginRole(LoginRole loginRole) {
		int count = getSqlMapClientTemplate().update("editLoginRole", loginRole);
		logger.info("Edited login role id: " + loginRole.getId() + ", rows affected: " + count);
	}

	@Override
	public void removeLoginRole(int loginRoleId) {
		int count = getSqlMapClientTemplate().delete("removeLoginRole", loginRoleId);
		logger.info("Removed login role: " + loginRoleId + ", rows affected: " + count);
	}
	
	@Override
	public void removeLoginRole(LoginRole LoginRole) {
		int count = getSqlMapClientTemplate().delete("removeLoginRole", LoginRole.getId());
		logger.info("Removed login role: " + LoginRole.getId() + ", rows affected: " + count);
	}

	@Override
	public LoginRole getLoginRoleById(int loginRoleId) {
		LoginRole loginRole = (LoginRole) getSqlMapClientTemplate().queryForObject("getLoginRoleById", loginRoleId);
		logger.info("Get login role with id: " + loginRoleId);
		return loginRole;
	}

	@Override
	public LoginRole getLoginRoleByName(String name) {
		LoginRole loginRole = (LoginRole) getSqlMapClientTemplate().queryForObject("getLoginRoleByName", name);
		logger.info("Get login role with name: " + name);
		return loginRole;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LoginRole> getLoginRoleList() {
		List<LoginRole> list = getSqlMapClientTemplate().queryForList("getLoginRoleList");
		logger.info("Get login role list, size: " + list.size());
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<LoginRole> getLoginRoleListByPersonUid(String personUid) {
		List<LoginRole> list = getSqlMapClientTemplate().queryForList("getLoginRoleListByPersonUid", personUid);
		logger.info("Get login role list by person uid: " + personUid + ", size: " + list.size());
		return list;
	}
}
