/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisFacility class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis.support;

import ca.sciencestudio.model.facility.Facility;

/**
 * @author maxweld
 *
 */
public class IbatisFacility {
	
	private static final int DEFAULT_ID = 0;
	
	private int id = DEFAULT_ID;
	private String name = Facility.DEFAULT_NAME;
	private String longName = Facility.DEFAULT_LONG_NAME;
	private String description = Facility.DEFAULT_DESCRIPTION;
	private String phoneNumber = Facility.DEFAULT_PHONE_NUMBER;
	private String emailAddress = Facility.DEFAULT_EMAIL_ADDRESS;
	private String location = Facility.DEFAULT_LOCATION;
	private String loginUrl = Facility.DEFAULT_LOGIN_URL;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
