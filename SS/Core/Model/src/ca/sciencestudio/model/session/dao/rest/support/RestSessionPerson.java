/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSession class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest.support;

import ca.sciencestudio.model.session.SessionPerson.Role;

/**
 * @author maxweld
 * 
 *
 */
public class RestSessionPerson {

	private String personGid;
	private String sessionGid;
	private Role role;
	
	public String getPersonGid() {
		return personGid;
	}
	public void setPersonGid(String personGid) {
		this.personGid = personGid;
	}
	
	public String getSessionGid() {
		return sessionGid;
	}
	public void setSessionGid(String sessionGid) {
		this.sessionGid = sessionGid;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
