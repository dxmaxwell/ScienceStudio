/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Account interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.util.Date;

import ca.sciencestudio.login.model.Model;

/**
 * @author maxweld
 *
 */
public final class Account implements Model {

	private static final long serialVersionUID = 1L;

	public static enum Status { ACTIVE, DISABLED, EXPIRED }
	
	public static final int DEFAULT_ID = 0;
	public static final String DEFAULT_USERNAME = "";
	public static final String DEFAULT_PASSWORD = "";
	public static final String DEFAULT_PERSON_GID = "";
	public static final String DEFAULT_STATUS = "UNKNOWN";
	public static final Date DEFAULT_CREATION_DATE = new Date();
	
	private int id = DEFAULT_ID;
	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;
	private String personGid = DEFAULT_PERSON_GID;
	private String status = DEFAULT_STATUS;
	private Date creationDate = DEFAULT_CREATION_DATE;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPersonGid() {
		return personGid;
	}
	public void setPersonGid(String personGid) {
		this.personGid = personGid;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
