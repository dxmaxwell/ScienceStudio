/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Person class.
 *     
 */
package ca.sciencestudio.model;

import java.util.Date;

import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 * 
 *
 */
public class Person implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "U";
	
	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_TITLE = 10;
	public static final int MAX_LENGTH_FIRST_NAME = 60;
	public static final int MAX_LENGTH_MIDDLE_NAME = 60;
	public static final int MAX_LENGTH_LAST_NAME = 60;
	public static final int MAX_LENGTH_PHONE_NUMBER = 50;
	public static final int MAX_LENGTH_MOBILE_NUMBER = 50;
	public static final int MAX_LENGTH_EMAIL_ADDRESS = 255;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_TITLE = ""; 
	public static final String DEFAULT_FIRST_NAME = "";
	public static final String DEFAULT_MIDDLE_NAME = "";
	public static final String DEFAULT_LAST_NAME = "";
	public static final String DEFAULT_PHONE_NUMBER = "";
	public static final String DEFAULT_MOBILE_NUMBER = "";
	public static final String DEFAULT_EMAIL_ADDRESS = "";
	public static final Date DEFAULT_MODIFICATION_DATE = new Date(0);

	private String gid = DEFAULT_GID;
	private String title = DEFAULT_TITLE;
	private String firstName = DEFAULT_FIRST_NAME;
	private String middleName = DEFAULT_MIDDLE_NAME;
	private String lastName = DEFAULT_LAST_NAME;
	private String phoneNumber = DEFAULT_PHONE_NUMBER;
	private String mobileNumber = DEFAULT_MOBILE_NUMBER;
	private String emailAddress = DEFAULT_EMAIL_ADDRESS;
	private Date modificationDate = DEFAULT_MODIFICATION_DATE;
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		modificationDate = new Date();
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		modificationDate = new Date();
	}
	
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
		modificationDate = new Date();
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
		modificationDate = new Date();
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		modificationDate = new Date();
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
		modificationDate = new Date();
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		modificationDate = new Date();
	}
	
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
}
