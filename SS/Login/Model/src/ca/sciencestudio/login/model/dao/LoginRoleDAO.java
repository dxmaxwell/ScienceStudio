/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AuthorityDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.login.model.dao.ModelDAO;

/**
 * @author maxweld
 *
 */
public interface LoginRoleDAO extends ModelDAO<LoginRole> {
	
	public LoginRole getByName(String name);
	
	public List<LoginRole> getAllByPersonGid(String personGid);
}
