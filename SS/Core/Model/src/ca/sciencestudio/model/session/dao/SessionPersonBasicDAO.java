/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.SessionPerson;

/**
 * @author maxweld
 * 
 *
 */
public interface SessionPersonBasicDAO extends ModelBasicDAO<SessionPerson> {

	public List<SessionPerson> getAllByPersonGid(String personGid);
	public List<SessionPerson> getAllBySessionGid(String sessionGid);
	//public List<SessionPerson> getAllBySessionMember(String personGid);
}
