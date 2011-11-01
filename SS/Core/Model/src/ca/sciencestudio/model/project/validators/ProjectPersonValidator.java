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
		
		/* Removed until personGid is converted to the new format *
		errors.pushNestedPath("personGid");
		GID gid = GID.parse(projectPerson.getGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Person GID field is required.");
		}
		else if(!gid.isType(ProjectPerson.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Person GID field must have type: " + ProjectPerson.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Person GID field must have a facility specifed.");
		}
		errors.popNestedPath();
		*/
		
		/* Removed until personGid is converted to the new format *
		errors.pushNestedPath("projectGid");
		if(projectPerson.getProjectId() <= 0) {
			errors.rejectValue(null, EC_REQUIRED, "Project ID field must be greater than zero.");
		}
		errors.popNestedPath();
		*/
		
		errors.pushNestedPath("role");
		try {
			ProjectPerson.Role.valueOf(projectPerson.getRole());
		}
		catch(Exception e) {
			errors.rejectValue(null, EC_REQUIRED, "Role field not in list: " + Arrays.toString(ProjectPerson.Role.values()));
		}
		errors.popNestedPath();
	}

	@Override
	protected Class<ProjectPerson> getModelClass() {
		return ProjectPerson.class;
	}
}
