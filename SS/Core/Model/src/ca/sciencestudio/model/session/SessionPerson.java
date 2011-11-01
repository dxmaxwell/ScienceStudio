/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPerson class.
 *     
 */
package ca.sciencestudio.model.session;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 * 
 *
 */
public class SessionPerson implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "H";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PERSON_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_SESSION_GID = GID.DEFAULT_GID;
	
	private String gid = DEFAULT_GID;
	private String personGid = DEFAULT_PERSON_GID;
	private String sessionGid = DEFAULT_SESSION_GID;
	
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
}
