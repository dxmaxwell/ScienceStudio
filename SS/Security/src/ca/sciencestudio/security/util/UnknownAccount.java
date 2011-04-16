/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    UnknownAccount class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.login.model.Account;

/**
 * @author maxweld
 *
 */
public class UnknownAccount implements Account {

	private int id;
	private String username;
	private String password;
	private String personUid;
	private Status status;
	private Date creationDate;
	
	
	private Log logger = LogFactory.getLog(getClass());
	
	protected void logAttemptedSet(String field) {
		logger.warn("Attempted to set read-only '" + field + "' property of UnknownAccount.");	
	}
	
	public UnknownAccount() {
		id = 0;
		username = "unknown@x.y.z";
		password = "UnknownPassword";
		personUid = "unknown@x.y.z";
		status = Status.UNKNOWN;
		creationDate = new Date();
	}

	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		logAttemptedSet("id");
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public void setUsername(String username) {
		logAttemptedSet("username");
	}

	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public void setPassword(String password) {
		logAttemptedSet("password");
	}

	@Override
	public String getPersonUid() {
		return personUid;
	}
	@Override
	public void setPersonUid(String personUid) {
		logAttemptedSet("personUid");
	}

	@Override
	public Status getStatus() {
		return status;
	}
	@Override
	public void setStatus(Status status) {
		logAttemptedSet("status");
	}
	
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
}
