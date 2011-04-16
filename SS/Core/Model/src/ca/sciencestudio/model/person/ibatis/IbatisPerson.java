/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisPerson class.
 *     
 */
package ca.sciencestudio.model.person.ibatis;

import java.util.Date;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public class IbatisPerson implements Person {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String uid;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneNumber;
	private String mobileNumber;
	private String emailAddress;
	private Date modificationDate;
	
	public IbatisPerson() {
		modificationDate = new Date();
	}
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getUid() {
		return uid;
	}
	@Override
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String getFirstName() {
		return firstName;
	}
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Override
	public String getMiddleName() {
		return middleName;
	}
	@Override
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	@Override
	public String getLastName() {
		return lastName;
	}
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getMobileNumber() {
		return mobileNumber;
	}
	@Override
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
}
