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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import ca.sciencestudio.login.model.Model;

/**
 * @author maxweld
 *
 */
public final class LoginHistory implements Model {

	private static final long serialVersionUID = 1L;
	
	private static final String HEADER_KEY_USER_AGENT = "user-agent";
	private static final String UNKNOWN_USER_AGENT = "Unknown User Agent";
	
	public enum Status { LOGIN_SUCCESS, LOGIN_FAILURE, LOGOUT_SUCCESS, LOGOUT_FAILURE }
	
	public int id;
	public String username;
	public String address;
	public int port;
	public String agent; 
	public Date timestamp;
	public String status;
	
	public LoginHistory() {}
	
	public LoginHistory(String username, HttpServletRequest request, Status status) {
		this.username = username;
		this.address = request.getRemoteAddr();
		this.port = request.getRemotePort();
		this.agent = request.getHeader(HEADER_KEY_USER_AGENT);
		if(this.agent == null) { this.agent = UNKNOWN_USER_AGENT; }
		this.timestamp = new Date();
		this.status = status.name();
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

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
