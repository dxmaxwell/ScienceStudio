/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginGroupDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.login.model.LoginGroup;
import ca.sciencestudio.login.model.dao.LoginGroupDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class IbatisLoginGroupDAO extends AbstractIbatisModelDAO<LoginGroup> implements LoginGroupDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<LoginGroup> getAllByPersonGid(String personGid) {
		
		List<LoginGroup> loginGroups;
		try {
			loginGroups = getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonGid"), personGid);
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Login Groups with Person GID: " + personGid + ", size: " + loginGroups.size());
		}
		return Collections.unmodifiableList(loginGroups);
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "LoginGroup" + suffix;
	}
}
