/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AccountDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.Account;

/**
 * @author maxweld
 *
 */
public interface AccountDAO {
	public Account createAccount();
	
	public int addAccount(Account account);
	public void editAccount(Account account);
	public void removeAccount(int accountId);
	public void removeAccount(Account account);
	
	public Account getAccountById(int accountId);
	public Account getAccountByUsername(String username);
	
	public List<Account> getAccountList();
	public List<String> getAccountUsernameList();
}
