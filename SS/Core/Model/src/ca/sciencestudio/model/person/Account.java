/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Account interface.
 *     
 */
package ca.sciencestudio.model.person;

import java.util.Date;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public final class Account implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "Z";
	
	public static enum Status { ACTIVE, DISABLED, EXPIRED }
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_USERNAME = "";
	public static final String DEFAULT_PASSWORD = "";
	public static final String DEFAULT_PERSON_GID = "";
	public static final Status DEFAULT_STATUS = Status.DISABLED;
	public static final Date DEFAULT_CREATION_DATE = new Date();
	
	private String gid = DEFAULT_GID;
	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;
	private String personGid = DEFAULT_PERSON_GID;
	private Status status = DEFAULT_STATUS;
	private Date creationDate = DEFAULT_CREATION_DATE;
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
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
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
