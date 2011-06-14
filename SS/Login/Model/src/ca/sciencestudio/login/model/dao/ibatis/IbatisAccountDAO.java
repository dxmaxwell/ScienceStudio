/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisAccountDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.login.model.Account;
import ca.sciencestudio.login.model.dao.AccountDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class IbatisAccountDAO extends AbstractIbatisModelDAO<Account> implements AccountDAO {
	
	@Override
	public Account getByUsername(String username) {
		
		Account account;
		try {
			account = (Account) getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByUsername"), username);
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Account with Username: " + username);
		}
		return account;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getAllUsernames() {
		
		List<String> usernames;
		try {
			usernames = getSqlMapClientTemplate().queryForList(getStatementName("get", "UsernameList"));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Usernames, size: " + usernames.size());
		}
		return Collections.unmodifiableList(usernames);
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Account" + suffix;
	}
}
