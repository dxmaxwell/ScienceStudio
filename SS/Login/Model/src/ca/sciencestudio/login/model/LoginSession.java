/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginSession implementation.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;
import java.util.Date;

import ca.sciencestudio.login.model.Model;

/**
 * @author maxweld
 *  
 */
public final class LoginSession implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_ID = 0;
	public static final String DEFAULT_SESSION_UUID = "00000000-0000-0000-0000-000000000000";
	public static final String DEFAULT_SESSION_DATA = "THE SESSION DATA HAS NOT BEEN INITIALIZED";
	public static final Date DEFAULT_TIMESTAMP = new Date(0);
	
	private int id = DEFAULT_ID;
	private String sessionUuid = DEFAULT_SESSION_UUID;
	private Serializable sessionData = DEFAULT_SESSION_DATA;
	private Date timestamp = DEFAULT_TIMESTAMP;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSessionUuid() {
		return sessionUuid;
	}
	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}
	
	public Serializable getSessionData() {
		return sessionData;
	}
	public void setSessionData(Serializable sessionData) {
		this.sessionData = sessionData;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
