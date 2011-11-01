/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Laboratory class.
 *     
 */
package ca.sciencestudio.model.facility;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class Laboratory implements Model {
	
	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "L";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_FACILITY_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_LONG_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_PHONE_NUMBER = "";
	public static final String DEFAULT_EMAIL_ADDRESS = "";
	public static final String DEFAULT_LOCATION = "";
	public static final String DEFAULT_VIEW_URL = "";

	private String gid;
	private String facilityGid;
	private String name;
	private String longName;
	private String description;
	private String phoneNumber;
	private String emailAddress;
	private String location;
	private String viewUrl;
	
	public Laboratory() {
		gid = DEFAULT_GID;
		facilityGid = DEFAULT_FACILITY_GID;
		name = DEFAULT_NAME;
		longName = DEFAULT_LONG_NAME;
		description = DEFAULT_DESCRIPTION;
		phoneNumber = DEFAULT_PHONE_NUMBER;
		emailAddress = DEFAULT_EMAIL_ADDRESS;
		location = DEFAULT_LOCATION;
		viewUrl = DEFAULT_VIEW_URL;
	}
	
	public Laboratory(Laboratory laboratory) {
		gid = laboratory.getGid();
		facilityGid = laboratory.getFacilityGid();
		name = laboratory.getName();
		longName = laboratory.getLongName();
		description = laboratory.getDescription();
		phoneNumber = laboratory.getPhoneNumber();
		emailAddress = laboratory.getEmailAddress();
		location = laboratory.getLocation();
		viewUrl = laboratory.getViewUrl();
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getFacilityGid() {
		return facilityGid;
	}
	public void setFacilityGid(String facilityGid) {
		this.facilityGid = facilityGid;
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
	
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
}
