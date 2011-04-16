/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      Credentials form backer class.
 *     
 */
package ca.sciencestudio.login.service.backers;

/**
 * @author maxweld
 *
 */
public class Credentials {
	
	private String password = "";
	private String username = "";
	private String domain = "";
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
