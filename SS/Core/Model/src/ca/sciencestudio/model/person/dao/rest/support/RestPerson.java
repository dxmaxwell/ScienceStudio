/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestPerson class.
 *     
 */
package ca.sciencestudio.model.person.dao.rest.support;

import java.util.Date;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 * 
 *
 */
public class RestPerson {

	private String title = Person.DEFAULT_TITLE;
	private String firstName = Person.DEFAULT_FIRST_NAME;
	private String middleName = Person.DEFAULT_MIDDLE_NAME;
	private String lastName = Person.DEFAULT_LAST_NAME;
	private String phoneNumber = Person.DEFAULT_PHONE_NUMBER;
	private String mobileNumber = Person.DEFAULT_MOBILE_NUMBER;
	private String emailAddress = Person.DEFAULT_EMAIL_ADDRESS;
	private Date modificationDate = Person.DEFAULT_MODIFICATION_DATE;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
}
