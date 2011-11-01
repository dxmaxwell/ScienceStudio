/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Facility class.
 *     
 */
package ca.sciencestudio.model.facility;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class Facility implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "F";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_LONG_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_PHONE_NUMBER = "";
	public static final String DEFAULT_EMAIL_ADDRESS = "";
	public static final String DEFAULT_LOCATION = "";
	public static final String DEFAULT_LOGIN_URL = "";
	
	private String gid = DEFAULT_GID;
	private String name = DEFAULT_NAME;
	private String longName = DEFAULT_LONG_NAME;
	private String description = DEFAULT_DESCRIPTION;
	private String phoneNumber = DEFAULT_PHONE_NUMBER;
	private String emailAddress = DEFAULT_EMAIL_ADDRESS;
	private String location = DEFAULT_LOCATION;
	private String loginUrl = DEFAULT_LOGIN_URL;
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}
