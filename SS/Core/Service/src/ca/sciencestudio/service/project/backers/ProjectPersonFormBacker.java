/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonFormBacker class.
 *     
 */
package ca.sciencestudio.service.project.backers;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 *
 */
public class ProjectPersonFormBacker extends ProjectPerson {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_FULL_NAME = "";
	
	private String fullName;
	private String emailAddress;
	private String phoneNumber;
	private String mobileNumber;
	
	public static ValidationResult transformResult(ValidationResult result) {
		return result;
	}
	
	public ProjectPersonFormBacker() {
		setFullName(DEFAULT_FULL_NAME);
		setEmailAddress(Person.DEFAULT_EMAIL_ADDRESS);
		setPhoneNumber(Person.DEFAULT_PHONE_NUMBER);
		setMobileNumber(Person.DEFAULT_MOBILE_NUMBER);
	}
	
//	public ProjectPersonFormBacker(int projectId, String projectRole) {
//		
//		setId(0);
//		setProjectId(projectId);
//		setProjectRole(projectRole);
//		
//		setPersonUid("");
//		setFullName("");
//		setEmailAddress("");
//		setPhoneNumber("");
//		setMobileNumber("");
//	}
	
	public ProjectPersonFormBacker(String projectGid, Role projectRole, Person person) {
		setRole(projectRole);
		setProjectGid(projectGid);
		setPersonGid(person.getGid());
		setFullName(Person.getFullName(person));
		setEmailAddress(person.getEmailAddress());
		setPhoneNumber(person.getPhoneNumber());
		setMobileNumber(person.getMobileNumber());
	}

	public ProjectPersonFormBacker(ProjectPerson projectPerson, Person person) {
		super(projectPerson);
		
		if(!getPersonGid().equals(person.getGid())) {
			throw new IllegalArgumentException();
		}
		
		setFullName(Person.getFullName(person, true));
		setEmailAddress(person.getEmailAddress());
		setPhoneNumber(person.getPhoneNumber());
		setMobileNumber(person.getMobileNumber());
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
}
