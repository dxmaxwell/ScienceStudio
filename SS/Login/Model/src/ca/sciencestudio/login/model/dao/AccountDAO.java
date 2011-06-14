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
public interface AccountDAO extends ModelDAO<Account> {
	
	public Account getByUsername(String username);
	
	public List<String> getAllUsernames();
}
