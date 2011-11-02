/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroupDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.LoginGroup;

import ca.sciencestudio.login.model.dao.ModelDAO;

/**
 * @author maxweld
 *
 */
public interface LoginGroupDAO extends ModelDAO<LoginGroup> {
	
	public List<LoginGroup> getAllByPersonGid(String personGid);
}
