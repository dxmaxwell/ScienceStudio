/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupMemberDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import ca.sciencestudio.login.model.LoginGroupMember;
import ca.sciencestudio.login.model.dao.LoginGroupMemberDAO;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupMemberDAO extends AbstractIbatisModelDAO<LoginGroupMember> implements LoginGroupMemberDAO {

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "LoginGroupMember" + suffix;
	}
}
