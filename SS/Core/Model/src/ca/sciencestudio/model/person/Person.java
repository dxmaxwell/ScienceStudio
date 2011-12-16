/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Person class.
 *     
 */
package ca.sciencestudio.model.person;

import java.util.Date;

import ca.sciencestudio.model.Model;
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
	
	// Utilities for building full and complete names. //
	public static String getFullName(Person person) {
		return getFullName(person, false);
	}
	
	public static String getFullName(Person person, boolean initial) {
		StringBuffer buffer = new StringBuffer();
		if(person.getFirstName() != null) {
			buffer.append(person.getFirstName().trim());
		}
		if(initial && (person.getMiddleName() != null)) {
			String middleName = person.getMiddleName().trim();
			if(middleName.length() > 0) {
				String middleInitial = middleName.substring(0,1);
				buffer.append(" ").append(middleInitial).append(".");
			}
		}
		if(person.getLastName() != null) {
			buffer.append(" ").append(person.getLastName().trim());
		}
		return buffer.toString();
	}
	
	public static String getCompleteName(Person person) {
		StringBuffer buffer = new StringBuffer();
		if(person.getFirstName() != null) {
			buffer.append(person.getFirstName().trim());
		}
		if(person.getMiddleName() != null) {
			buffer.append(" ").append(person.getMiddleName().trim());
		}
		if(person.getLastName() != null) {
			buffer.append(" ").append(person.getLastName().trim());
		}
		return buffer.toString();
	}
	////////////////////////////////////////////////////////
	
	private String gid;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneNumber;
	private String mobileNumber;
	private String emailAddress;
	private Date modificationDate;
	
	public Person() {
		gid = DEFAULT_GID;
		title = DEFAULT_TITLE;
		firstName = DEFAULT_FIRST_NAME;
		middleName = DEFAULT_MIDDLE_NAME;
		lastName = DEFAULT_LAST_NAME;
		phoneNumber = DEFAULT_PHONE_NUMBER;
		mobileNumber = DEFAULT_MOBILE_NUMBER;
		emailAddress = DEFAULT_EMAIL_ADDRESS;
		modificationDate = DEFAULT_MODIFICATION_DATE;
	}
	
	public Person(Person person) {
		gid = person.getGid();
		title = person.getTitle();
		firstName = person.getFirstName();
		middleName = person.getMiddleName();
		lastName = person.getLastName();
		phoneNumber = person.getPhoneNumber();
		mobileNumber = person.getMobileNumber();
		emailAddress = person.getEmailAddress();
		modificationDate = person.getModificationDate();
	}
	
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
