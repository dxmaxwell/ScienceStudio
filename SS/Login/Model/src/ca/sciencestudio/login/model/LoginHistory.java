/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginHistory class.
 *     
 *     Depreciated, but useful for future development.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * @author maxweld
 *
 */
public class LoginHistory  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String HEADER_KEY_USER_AGENT = "user-agent";
	private static final String UNKNOWN_USER_AGENT = "Unknown User Agent";
	
	public enum Status {
		LOGIN_SUCCESS,
		LOGIN_FAILURE,
		LOGOUT_SUCCESS,
		LOGOUT_FAILURE
	}
	
	public int id;
	public String username;
	public String address;
	public int port;
	public String agent; 
	public Date timestamp;
	public Status status;
	
	public LoginHistory() {}
	
	public LoginHistory(String username, HttpServletRequest request, Status status) {
		this.username = username;
		this.address = request.getRemoteAddr();
		this.port = request.getRemotePort();
		this.agent = request.getHeader(HEADER_KEY_USER_AGENT);
		if(this.agent == null) { this.agent = UNKNOWN_USER_AGENT; }
		this.timestamp = new Date();
		setStatus(status);
	}
	
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

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getStatusString() {
		return status.name();
	}
	public void setStatus(String status) {
		this.status = Status.valueOf(status);
	}
}
