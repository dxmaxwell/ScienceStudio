/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginRoleDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.login.model.dao.LoginRoleDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class IbatisLoginRoleDAO extends AbstractIbatisModelDAO<LoginRole> implements LoginRoleDAO {

	@Override
	public LoginRole getByName(String name) {
		
		LoginRole loginRole;
		try {
			loginRole = (LoginRole)getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByName"), name);
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Login Role with Name: " + name);
		}
		return loginRole;
	}	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<LoginRole> getAllByPersonGid(String personGid) {
		
		List<LoginRole> loginRoles;
		try {
			loginRoles = getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonGid"), personGid);
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Login Roles with Person GID: " + personGid + ", size: " + loginRoles.size());
		}
		return Collections.unmodifiableList(loginRoles);
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "LoginRole" + suffix;
	}
}
