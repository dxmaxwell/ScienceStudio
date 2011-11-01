/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSessionPerson class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis.support;

import ca.sciencestudio.model.session.SessionPerson;

/**
 * @author maxweld
 * 
 *
 */
public class IbatisSessionPerson {

	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_SESSION_ID = 0;
	
	private int id = DEFAULT_ID;
	private String personGid = SessionPerson.DEFAULT_PERSON_GID;
	private int sessionId = DEFAULT_SESSION_ID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPersonGid() {
		return personGid;
	}
	public void setPersonGid(String personGid) {
		this.personGid = personGid;
	}
	
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
}
