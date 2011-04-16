/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ProjectPersonFormValidator class.
 *     
 */
package ca.sciencestudio.service.project.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonDAO;
import ca.sciencestudio.model.project.ProjectRole;
import ca.sciencestudio.service.project.backers.ProjectPersonFormBacker;

/**
 * @author maxweld
 *
 */
public class ProjectPersonFormValidator implements Validator {

	@Autowired
	private PersonDAO personDAO;
	
	public boolean supports(Class<?> clazz) {
		return ProjectPersonFormBacker.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		ProjectPersonFormBacker projectPersonBacker = (ProjectPersonFormBacker) obj;
		if(projectPersonBacker == null) {
			errors.reject("", "Project person backer is null.");
			return;
		}
		
		Person person = personDAO.getPersonByUid(projectPersonBacker.getPersonUid());
		if(person == null) {
			errors.rejectValue("personUid", "", "Person is required.");
		}
		
		if(projectPersonBacker.getProjectRole() == null) {
			projectPersonBacker.setProjectRole(ProjectRole.UNKNOWN);
		}
		
		if(projectPersonBacker.getProjectRole() == ProjectRole.UNKNOWN) {
			errors.rejectValue("projectRole", "", "Project role is required.");
		}
	}

	public PersonDAO getPersonDAO() {
		return personDAO;
	}
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
}
