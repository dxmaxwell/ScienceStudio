/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPerson class.
 *     
 */
package ca.sciencestudio.model.session;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.session.validators.SessionPersonValidator;

/**
 * @author maxweld
 * 
 *
 */
public class SessionPerson implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "H";
	
	public static enum Role { OBSERVER, EXPERIMENTER }
	
	private String gid;
	private String personGid;
	private String sessionGid;
	private Role role;
	
	public SessionPerson() {
		gid = SessionPersonValidator.DEFAULT_GID;
		personGid = SessionPersonValidator.DEFAULT_PERSON_GID;
		sessionGid = SessionPersonValidator.DEFAULT_SESSION_GID;
		role = SessionPersonValidator.DEFAULT_ROLE;
	}
	
	public SessionPerson(SessionPerson sessionPerson) {
		gid = sessionPerson.getGid();
		personGid = sessionPerson.getPersonGid();
		sessionGid = sessionPerson.getSessionGid();
		role = sessionPerson.getRole();
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
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
