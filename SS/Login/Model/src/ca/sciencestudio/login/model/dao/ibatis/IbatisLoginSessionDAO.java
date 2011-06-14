/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginSessionDao implementation.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.login.model.LoginSession;
import ca.sciencestudio.login.model.dao.LoginSessionDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *  
 */
public class IbatisLoginSessionDAO extends AbstractIbatisModelDAO<LoginSession> implements LoginSessionDAO {

	@Override
	public LoginSession getByUuid(String uuid) {
		
		LoginSession t;
		try {
			t = (LoginSession)getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByUuid"), uuid);
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Login Sesion with UUID: " + uuid);
		}
		return t;
	}
	
	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "LoginSession" + suffix;
	}
}
