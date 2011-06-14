/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupRoleDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import ca.sciencestudio.login.model.LoginGroupRole;
import ca.sciencestudio.login.model.dao.LoginGroupRoleDAO;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupRoleDAO extends AbstractIbatisModelDAO<LoginGroupRole> implements LoginGroupRoleDAO {

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "LoginGroupRole" + suffix;
	}
}
