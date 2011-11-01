/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionAuthorityAccessor class.
 *     
 */
package ca.sciencestudio.security.authz.accessors;

import java.util.List;

import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;
import ca.sciencestudio.security.authz.AccessorAuthorites;

/**
 * @author maxweld
 * 
 *
 */
public class SessionAuthorityAccessor extends FacilityAuthorityAccessor {
	
	private SessionPersonBasicDAO sessionPersonBasicDAO;

	@Override
	public AccessorAuthorites getAuthorities(String user, String gid) {
		
		AccessorAuthorites authorities = getAuthorities(user);
		
		List<SessionPerson> sessionPersons = sessionPersonBasicDAO.getAllBySessionGid(gid);
		for(SessionPerson sessionPerson : sessionPersons) {
			if(sessionPerson.getPersonGid().equalsIgnoreCase(user)) {
				authorities.addSessionAuthority(sessionPerson.getRole());
			}
		}
		
		return authorities;
	}
	
	public SessionPersonBasicDAO getSessionPersonBasicDAO() {
		return sessionPersonBasicDAO;
	}
	public void setSessionPersonBasicDAO(SessionPersonBasicDAO sessionPersonBasicDAO) {
		this.sessionPersonBasicDAO = sessionPersonBasicDAO;
	}
}
