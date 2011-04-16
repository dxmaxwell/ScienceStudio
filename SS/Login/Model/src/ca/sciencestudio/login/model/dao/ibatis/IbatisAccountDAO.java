/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisAccountDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.login.model.Account;
import ca.sciencestudio.login.model.dao.AccountDAO;
import ca.sciencestudio.login.model.ibatis.IbatisAccount;

/**
 * @author maxweld
 *
 */
public class IbatisAccountDAO extends SqlMapClientDaoSupport implements AccountDAO {

	@Override
	public Account createAccount() {
		return new IbatisAccount();
	}
	
	@Override
	public int addAccount(Account account) {
		int key = (Integer) getSqlMapClientTemplate().insert("addAccount", account);
		logger.info("Added new account with id: " + key);
		return key;
	}

	@Override
	public void editAccount(Account account) {
		int count = getSqlMapClientTemplate().update("editAccount", account);
		logger.info("Edited account id: " + account.getId() + ", rows affected: " + count);
	}

	@Override
	public Account getAccountById(int accountId) {
		Account account = (Account) getSqlMapClientTemplate().queryForObject("getAccountById", accountId);
		logger.info("Get account with id: " + accountId);
		return account;
	}

	@Override
	public Account getAccountByUsername(String username) {
		Account account = (Account) getSqlMapClientTemplate().queryForObject("getAccountByUsername", username);
		logger.info("Get account with username: " + username);
		return account;
	}

	@Override
	public void removeAccount(int accountId) {
		int count = getSqlMapClientTemplate().delete("removeAccount", accountId);
		logger.info("Removed account: " + accountId + ", rows affected: " + count);
	}
	
	@Override
	public void removeAccount(Account account) {
		int count = getSqlMapClientTemplate().delete("removeAccount", account.getId());
		logger.info("Removed account: " + account.getId() + ", rows affected: " + count);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Account> getAccountList() {
		List<Account> list = getSqlMapClientTemplate().queryForList("getAccountList");
		logger.info("Get account list, size: " + list.size());
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getAccountUsernameList() {
		List<String> usernames = getSqlMapClientTemplate().queryForList("getAccountUsernameList");
		logger.info("Get account username list, size: " + usernames.size());
		return usernames;
	}
}
