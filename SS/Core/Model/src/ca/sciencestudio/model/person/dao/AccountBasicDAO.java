/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AccountDAO interface.
 *     
 */
package ca.sciencestudio.model.person.dao;

import java.util.List;

import ca.sciencestudio.model.person.Account;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 *
 */
public interface AccountBasicDAO extends ModelBasicDAO<Account> {
	
	public Account getByUsername(String username);
	
	public List<String> getAllUsernames();
}
