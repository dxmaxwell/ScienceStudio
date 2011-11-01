/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLaboratory class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis.support;

import ca.sciencestudio.model.facility.Laboratory;

/**
 * @author maxweld
 *
 */
public class IbatisLaboratory {
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_FACILITY_ID = 0;
	
	private int id = DEFAULT_ID;
	private int facilityId = DEFAULT_FACILITY_ID;
	private String name = Laboratory.DEFAULT_NAME;
	private String longName = Laboratory.DEFAULT_LONG_NAME;
	private String description = Laboratory.DEFAULT_DESCRIPTION;
	private String phoneNumber = Laboratory.DEFAULT_PHONE_NUMBER;
	private String emailAddress = Laboratory.DEFAULT_EMAIL_ADDRESS;
	private String location = Laboratory.DEFAULT_LOCATION;
	private String viewUrl = Laboratory.DEFAULT_VIEW_URL;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
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
