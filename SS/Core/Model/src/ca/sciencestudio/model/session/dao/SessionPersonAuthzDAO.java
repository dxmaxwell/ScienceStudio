/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.session.SessionPerson;

/**
 * @author maxweld
 * 
 *
 */
public interface SessionPersonAuthzDAO extends ModelAuthzDAO<SessionPerson> {

	public Data<List<SessionPerson>> getAllBySessionGid(String user, String sessionGid);
}
