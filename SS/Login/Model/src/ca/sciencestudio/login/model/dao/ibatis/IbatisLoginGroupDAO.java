/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.LoginGroup;
import ca.sciencestudio.login.model.dao.LoginGroupDAO;
import ca.sciencestudio.login.model.ibatis.IbatisLoginGroup;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupDAO extends SqlMapClientDaoSupport implements LoginGroupDAO {

	@Override
	public LoginGroup createLoginGroup() {
		return new IbatisLoginGroup();
	}

	@Override
	public int addLoginGroup(LoginGroup loginGroup) {
		int key = (Integer) getSqlMapClientTemplate().insert("addLoginGroup", loginGroup);
		logger.debug("Added new login group with id: " + key);
		return key;
	}

	@Override
	public void editLoginGroup(LoginGroup loginGroup) {
		int count = getSqlMapClientTemplate().update("editLoginGroup", loginGroup);
		logger.debug("Edited login group id: " + loginGroup.getId() + ", rows affected: " + count);
	}

	@Override
	public void removeLoginGroup(int loginGroupId) {
		int count = getSqlMapClientTemplate().delete("removeLoginGroup", loginGroupId);
		logger.debug("Removed login group: " + loginGroupId + ", rows affected: " + count);		
	}
	
	@Override
	public void removeLoginGroup(LoginGroup loginGroup) {
		int count = getSqlMapClientTemplate().delete("removeLoginGroup", loginGroup.getId());
		logger.debug("Removed login group: " + loginGroup.getId() + ", rows affected: " + count);
	}

	@Override
	public LoginGroup getLoginGroupById(int loginGroupId) {
		LoginGroup LoginGroup = (LoginGroup) getSqlMapClientTemplate().queryForObject("getLoginGroupById", loginGroupId);
		logger.debug("Get login group: " + loginGroupId);
		return LoginGroup;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LoginGroup> getLoginGroupList() {
		List<LoginGroup> list = getSqlMapClientTemplate().queryForList("getLoginGroupList");
		logger.debug("Get Login group list, size: " + list.size());
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<LoginGroup> getLoginGroupListByPersonUid(String personUid) {
		List<LoginGroup> list = getSqlMapClientTemplate().queryForList("getLoginGroupListByPersonUid", personUid);
		logger.debug("Get Login group list with person uid: " + personUid + ", size: " + list.size());
		return list;
	}
}
