/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisAccountDAO class.
 *     
 */
package ca.sciencestudio.model.person.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.person.Account;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.AccountBasicDAO;
import ca.sciencestudio.model.person.dao.ibatis.support.IbatisAccount;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class IbatisAccountBasicDAO extends AbstractIbatisModelBasicDAO<Account> implements AccountBasicDAO {
	
	@Override
	public String getGidType() {
		return Account.GID_TYPE;
	}
	
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

	@Override
	protected Object toIbatisModel(Account account) {
		if(account == null) {
			return null;
		}
		IbatisAccount ibatisAccount = new IbatisAccount();
		GID gid = GID.parse(account.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisAccount.setId(gid.getId());
		}
		ibatisAccount.setUsername(account.getUsername());
		ibatisAccount.setPassword(account.getPassword());
		gid = GID.parse(account.getPersonGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), Person.GID_TYPE, true, true)) {
			ibatisAccount.setPersonId(gid.getId());
		}
		ibatisAccount.setStatus(account.getStatus().name());
		ibatisAccount.setCreationDate(account.getCreationDate());
		return ibatisAccount;
	}
	
	@Override
	protected Account toModel(Object obj) {
		if(!(obj instanceof IbatisAccount)) {
			return null;
		}
		IbatisAccount ibatisAccount = (IbatisAccount)obj;
		Account account = new Account();
		account.setGid(GID.format(getGidFacility(), ibatisAccount.getId(), getGidType()));
		account.setUsername(ibatisAccount.getUsername());
		account.setPassword(ibatisAccount.getPassword());
		account.setPersonGid(GID.format(getGidFacility(), ibatisAccount.getPersonId(), Person.GID_TYPE));
		account.setStatus(Account.Status.valueOf(ibatisAccount.getStatus()));
		account.setCreationDate(ibatisAccount.getCreationDate());
		return account;
	}
}
