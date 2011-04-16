/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLaboratory class.
 *     
 */
package ca.sciencestudio.model.facility.ibatis;

import ca.sciencestudio.model.facility.Laboratory;

/**
 * @author maxweld
 *
 */
public class IbatisLaboratory implements Cloneable, Laboratory {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int facilityId;
	private String name;
	private String longName;
	private String description;
	private String phoneNumber;
	private String emailAddress;
	private String location;
	private String viewUrl;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getFacilityId() {
		return facilityId;
	}
	@Override
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getLongName() {
		return longName;
	}
	@Override
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String getPhoneNumber() {
		return phoneNumber;
	}
	@Override
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String getEmailAddress() {
		return emailAddress;
	}
	@Override
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Override
	public String getLocation() {
		return location;
	}
	@Override
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String getViewUrl() {
		return viewUrl;
	}
	@Override
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	
	@Override
	public Laboratory clone() {
		try {
			return (Laboratory) super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
