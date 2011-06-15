/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectValidator class.
 *     
 */
package ca.sciencestudio.model.validators;

import java.util.Arrays;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Project;

/**
 * @author maxweld
 *
 */
public class ProjectValidator extends AbstractModelValidator<Project> {
	
	@Override
	protected Class<Project> getModelClass() {
		return Project.class;
	}

	@Override
	public void validate(Project project, Errors errors) {
		
		errors.pushNestedPath("name");
		if(project.getName() == null) {
			project.setName(Project.DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespce.");
		rejectIfExceedsLength(Project.MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("description");
		if(project.getDescription() == null) {
			project.setDescription(Project.DEFAULT_DESCRIPTION);
		}
		rejectIfExceedsLength(Project.MAX_LENGTH_DESCRIPTION, errors, null, EC_MAX_LENGTH, "Description field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("startDate");
		if(project.getStartDate() == null) { 
			project.setStartDate(Project.DEFAULT_START_DATE);
		} 	
		if(project.getStartDate().equals(Project.DEFAULT_START_DATE)) {
			errors.rejectValue(null, EC_REQUIRED, "Start date field equals default.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("endDate");
		if(project.getEndDate() == null) {
			project.setEndDate(Project.DEFAULT_END_DATE);
		}
		if(project.getEndDate().equals(Project.DEFAULT_END_DATE)) {
			errors.rejectValue(null, EC_REQUIRED, "End date field equals default.");
		}
		errors.popNestedPath();
		
		if(!errors.hasFieldErrors("startDate") && !errors.hasFieldErrors("endDate") && !project.getStartDate().before(project.getEndDate())) {
			errors.rejectValue("endDate", EC_AFTER, "End Date field is equal or after Start Date.");
		}
		
		errors.pushNestedPath("status");
		try {
			Project.Status.valueOf(project.getStatus());
			rejectIfExceedsLength(Project.MAX_LENGTH_STATUS, errors, null, EC_MAX_LENGTH, "Status field exceeds maximum length.");
		}
		catch(Exception e) {
			errors.rejectValue(null, EC_INVALID, "Status field is not in list: " + Arrays.toString(Project.Status.values()));
		}
		errors.popNestedPath();
	}
}
