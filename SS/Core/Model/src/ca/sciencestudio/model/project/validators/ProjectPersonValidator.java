/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonValidator class.
 *     
 */
package ca.sciencestudio.model.project.validators;

import java.util.Arrays;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class ProjectPersonValidator extends AbstractModelValidator<ProjectPerson> {

	@Override
	public void validate(ProjectPerson projectPerson, Errors errors) {
		
		errors.pushNestedPath("personGid");
		GID gid = GID.parse(projectPerson.getPersonGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Person GID field is required.");
		}
		else if(!gid.isType(Person.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Person GID field must have type: " + Person.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Person GID field must have a facility specifed.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("projectGid");
		gid = GID.parse(projectPerson.getProjectGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Project GID field is required.");
		}
		else if(!gid.isType(Project.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Project GID field must have type: " + Project.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Project GID field must have a facility specifed.");
		}	
		errors.popNestedPath();
		
		errors.pushNestedPath("role");
		if(projectPerson.getRole() == null) {
			errors.rejectValue(null, EC_REQUIRED, "Role field not in list: " + Arrays.toString(ProjectPerson.Role.values()));
		}
		errors.popNestedPath();
	}

	@Override
	protected Class<ProjectPerson> getModelClass() {
		return ProjectPerson.class;
	}
}
