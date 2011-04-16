/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    UnknownPerson class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public class UnknownPerson implements Person {
	
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
	
	private Log logger = LogFactory.getLog(getClass());
	
	public UnknownPerson() {
		id = 0;
		uid = "unknown@x.y.z";
		firstName = "unknown";
		middleName = "";
		lastName = "unknown";
		phoneNumber = "555-555-1234";
		mobileNumber = "555-555-4321";
		emailAddress = "unknown@fake.za";
		modificationDate = new Date();
	}
	
	public UnknownPerson(String personUid) {
		id = 0;
		uid = personUid;
		firstName = personUid;
		middleName = "";
		lastName = "";
		phoneNumber = "555-555-1234";
		mobileNumber = "555-555-4321";
		emailAddress = "unknown@fake.za";
		modificationDate = new Date();
	}
	
	protected void logAttemptedSet(String field) {
		logger.warn("Attempted to set read-only '" + field + "' property of UnknownPerson.");	
	}
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		logAttemptedSet("id");
	}
	
	@Override
	public String getUid() {
		return uid;
	}
	@Override
	public void setUid(String uid) {
		logAttemptedSet("uid");
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public void setTitle(String title) {
		logAttemptedSet("title");
	}
	
	@Override
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		logAttemptedSet("firstName");
	}
	
	@Override
	public String getMiddleName() {
		return middleName;
	}
	@Override
	public void setMiddleName(String middleName) {
		logAttemptedSet("middleName");
	}
	
	@Override
	public String getLastName() {
		return lastName;
	}
	@Override
	public void setLastName(String lastName) {
		logAttemptedSet("lastName");
	}
	
	@Override
	public String getPhoneNumber() {
		return phoneNumber;
	}
	@Override
	public void setPhoneNumber(String phoneNumber) {
		logAttemptedSet("phoneNumber");
	}
	
	@Override
	public String getMobileNumber() {
		return mobileNumber;
	}
	@Override
	public void setMobileNumber(String mobileNumber) {
		logAttemptedSet("mobileNumber");
	}
	
	@Override
	public String getEmailAddress() {
		return emailAddress;
	}
	@Override
	public void setEmailAddress(String emailAddress) {
		logAttemptedSet("emailAddress");
	}
	
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}
}
