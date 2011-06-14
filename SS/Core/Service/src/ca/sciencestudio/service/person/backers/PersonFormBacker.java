/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      PersonFormBacker class.
 *     
 */
package ca.sciencestudio.service.person.backers;

import ca.sciencestudio.model.Person;

/**
 * @author maxweld
 *
 */
public class PersonFormBacker {
	
	private int id;
	private String uid;
	
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneNumber;
	private String mobileNumber;
	private String emailAddress;
	
	
	public PersonFormBacker() {
		setId(0);
		setUid("");
		
		setTitle("");
		setFirstName("");
		setMiddleName("");
		setLastName("");
		
		setPhoneNumber("");
		setMobileNumber("");
		setEmailAddress("");
	}
	
	public PersonFormBacker(Person person) {
		setId(0);
		setUid(person.getGid());
		
		setTitle(person.getTitle());
		setFirstName(person.getFirstName());
		setMiddleName(person.getMiddleName());
		setLastName(person.getLastName());
		
		setPhoneNumber(person.getPhoneNumber());
		setMobileNumber(person.getMobileNumber());
		setEmailAddress(person.getEmailAddress());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
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
}
