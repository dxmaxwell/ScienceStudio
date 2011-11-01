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
	public static final String DEFAULT_AUTHC_URL = "";
	public static final String DEFAULT_HOME_URL = "";
	
	private String gid;
	private String name;
	private String longName;
	private String description;
	private String phoneNumber;
	private String emailAddress;
	private String location;
	private String authcUrl;
	private String homeUrl;
	
	
	public Facility() {
		gid = DEFAULT_GID;
		name = DEFAULT_NAME;
		longName = DEFAULT_LONG_NAME;
		description = DEFAULT_DESCRIPTION;
		phoneNumber = DEFAULT_PHONE_NUMBER;
		emailAddress = DEFAULT_EMAIL_ADDRESS;
		location = DEFAULT_LOCATION;
		authcUrl = DEFAULT_AUTHC_URL;
		homeUrl = DEFAULT_HOME_URL;
	}
	
	public Facility(Facility facility) {
		gid = facility.getGid();
		name = facility.getName();
		longName = facility.getLongName();
		description = facility.getDescription();
		phoneNumber = facility.getPhoneNumber();
		emailAddress = facility.getEmailAddress();
		location = facility.getLocation();
		authcUrl = facility.getAuthcUrl();
		homeUrl = facility.getHomeUrl();
	}
		
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

	public String getAuthcUrl() {
		return authcUrl;
	}
	public void setAuthcUrl(String authcUrl) {
		this.authcUrl = authcUrl;
	}

	public String getHomeUrl() {
		return homeUrl;
	}
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
}
