/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisFacility class.
 *     
 */
package ca.sciencestudio.model.facility.ibatis;

import ca.sciencestudio.model.facility.Facility;

/**
 * @author maxweld
 *
 */
public class IbatisFacility implements Cloneable, Facility {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String longName;
	private String description;
	private String phoneNumber;
	private String emailAddress;
	private String location;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
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
	public Facility clone() {
		try {
			return (Facility) super.clone();
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}
}
