/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginSessionDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import ca.sciencestudio.login.model.LoginSession;
import ca.sciencestudio.login.model.dao.ModelDAO;

/**
 * @author maxweld
 *  
 */
public interface LoginSessionDAO extends ModelDAO<LoginSession> {
	
	public LoginSession getByUuid(String uuid);
}
