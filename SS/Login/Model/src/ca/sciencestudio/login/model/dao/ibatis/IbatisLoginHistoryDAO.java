/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginHistoryDAO implementation.
 *     
 *     Depreciated, but useful for future development.
 */
package ca.sciencestudio.login.model.dao.ibatis;

import ca.sciencestudio.login.model.LoginHistory;
import ca.sciencestudio.login.model.dao.LoginHistoryDAO;

/**
 * @author maxweld
 *
 */
public class IbatisLoginHistoryDAO extends AbstractIbatisModelDAO<LoginHistory> implements LoginHistoryDAO {

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "LoginHistory" + suffix;
	}
}
