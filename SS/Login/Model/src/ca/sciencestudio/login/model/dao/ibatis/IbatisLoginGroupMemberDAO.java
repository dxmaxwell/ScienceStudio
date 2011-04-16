/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupMemberDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.LoginGroupMember;
import ca.sciencestudio.login.model.dao.LoginGroupMemberDAO;
import ca.sciencestudio.login.model.ibatis.IbatisLoginGroupMember;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupMemberDAO extends SqlMapClientDaoSupport implements LoginGroupMemberDAO {

	@Override
	public LoginGroupMember createLoginGroupMember() {
		return new IbatisLoginGroupMember(); 
	}
	
	@Override
	public int addLoginGroupMember(LoginGroupMember loginGroupMember) {
		int key = (Integer) getSqlMapClientTemplate().insert("addLoginGroupMember", loginGroupMember);
		logger.info("Added new login group member with id: " + key);
		return key;
	}

	@Override
	public void editLoginGroupMember(LoginGroupMember loginGroupMember) {
		int count = getSqlMapClientTemplate().update("editLoginGroupMember", loginGroupMember);
		logger.info("Edited login group member id: " + loginGroupMember.getId() + ", rows affected: " + count);
	}

	@Override
	public void removeLoginGroupMember(int loginGroupMemberId) {
		int count = getSqlMapClientTemplate().delete("removeLoginGroupMember", loginGroupMemberId);
		logger.info("Removed login group member: " + loginGroupMemberId + ", rows affected: " + count);
	}

	@Override
	public void removeLoginGroupMember(LoginGroupMember loginGroupMember) {
		int count = getSqlMapClientTemplate().delete("removeLoginGroupMember", loginGroupMember.getId());
		logger.info("Removed login group member: " + loginGroupMember.getId() + ", rows affected: " + count);
	}

	@Override
	public LoginGroupMember getLoginGroupMemberById(int loginGroupMemberId) {
		LoginGroupMember loginGroupMember = (LoginGroupMember) getSqlMapClientTemplate().queryForObject("getLoginGroupMemberById", loginGroupMemberId);
		logger.info("Get login group member: " + loginGroupMemberId);
		return loginGroupMember;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LoginGroupMember> getLoginGroupMemberList() {
		List<LoginGroupMember> list = getSqlMapClientTemplate().queryForList("getLoginGroupMemberList");
		logger.info("Get login group member list, size: " + list.size());
		return list;
	}
}
