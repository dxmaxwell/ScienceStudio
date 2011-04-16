/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonBacker class.
 *     
 */
package ca.sciencestudio.service.project.backers;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectRole;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;

/**
 * @author maxweld
 *
 */
public class ProjectPersonFormBacker {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int projectId;
	private String personUid;
	private ProjectRole projectRole;
	private String fullName;
	private String emailAddress;
	private String phoneNumber;
	private String mobileNumber;
	
	public ProjectPersonFormBacker(int projectId, ProjectRole projectRole) {
		setId(0);
		setProjectId(projectId);
		setProjectRole(projectRole);
		
		setPersonUid("");
		setFullName("");
		setEmailAddress("");
		setPhoneNumber("");
		setMobileNumber("");
	}
	
	public ProjectPersonFormBacker(int projectId, ProjectRole projectRole, Person person) {
		setId(0);
		setProjectId(projectId);
		setProjectRole(projectRole);
		
		setPersonUid(person.getUid());
		setFullName(buildFullName(person));
		setEmailAddress(person.getEmailAddress());
		setPhoneNumber(person.getPhoneNumber());
		setMobileNumber(person.getMobileNumber());
	}
	
	public ProjectPersonFormBacker(ProjectPerson projectPerson, Person person) {
		if(!person.getUid().equals(projectPerson.getPersonUid())) {
			throw new IllegalArgumentException();
		}
		
		setId(projectPerson.getId());
		setProjectId(projectPerson.getProjectId());
		setProjectRole(projectPerson.getProjectRole());
		
		setPersonUid(person.getUid());
		setFullName(buildFullName(person));
		setEmailAddress(person.getEmailAddress());
		setPhoneNumber(person.getPhoneNumber());
		setMobileNumber(person.getMobileNumber());
	}
	
	public ProjectPerson createProjectPerson(ProjectPersonDAO projectPersonDAO) {
		ProjectPerson projectPerson = projectPersonDAO.createProjectPerson();
		projectPerson.setId(getId());
		projectPerson.setProjectId(getProjectId());
		projectPerson.setPersonUid(getPersonUid());
		projectPerson.setProjectRole(getProjectRole());
		return projectPerson;
	}
	
	protected String buildFullName(Person person) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(person.getFirstName());
		
		String middleName = person.getMiddleName();
		if((middleName != null) && (middleName.length() > 0)) {
			buffer.append(" ");
			buffer.append(middleName.substring(0,1));
			buffer.append(".");
		}
		
		buffer.append(" ");
		buffer.append(person.getLastName());
		return buffer.toString();
	}
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}
	
	public int getProjectId() {
		return projectId;
	}
	private void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public String getPersonUid() {
		return personUid;
	}
	public void setPersonUid(String personUid) {
		this.personUid = personUid;
	}
	
	public ProjectRole getProjectRole() {
		return projectRole;
	}
	public void setProjectRole(ProjectRole projectRole) {
		this.projectRole = projectRole;
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
