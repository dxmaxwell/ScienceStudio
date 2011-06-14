/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectFormBackerValidator class.
 *     
 */
package ca.sciencestudio.service.project.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.model.validators.ProjectValidator;
import ca.sciencestudio.service.project.backers.ProjectFormBacker;

/**
 * @author maxweld
 *
 */
public class ProjectFormBackerValidator implements Validator {
	
	private ProjectValidator projectValidator;
	
	public boolean supports(Class<?> clazz) {
		return ProjectFormBacker.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		if(!(obj instanceof ProjectFormBacker)) {
			errors.reject(ModelValidator.EC_CLASS_NOT_SUPPORTED, "Validator does not support class: " + obj.getClass());
			return;
		}
		
		ProjectFormBacker projectFormBacker = (ProjectFormBacker)obj;
		
		Errors projectErrors = projectValidator.validate(projectFormBacker.toProject());
		
		if(projectErrors.hasFieldErrors()) {
			for(FieldError fieldError : projectErrors.getFieldErrors()) {
				errors.rejectValue(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage());
			}
		}
		
		if(projectErrors.hasGlobalErrors()) {
			for(ObjectError objectError : projectErrors.getGlobalErrors()) {
				errors.reject(objectError.getCode(), objectError.getDefaultMessage());
			}
		}
		
		
		/*
		ProjectFormBacker projectFormBacker = (ProjectFormBacker) object;
		if(projectFormBacker == null) {
			errors.rejectValue("project", "", null, "Project is required.");
			return;
		}
		
		// validate 'name' field // 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Name is required.");
		
		if(!errors.hasFieldErrors("name") && (projectFormBacker.getName().length() > PROJECT_NAME_MAX_LENGTH)) {
			errors.rejectValue("name", "", "Specified name is too long.");
		}
		
		// validate 'description' field //
		if(projectFormBacker.getDescription() == null) {
			projectFormBacker.setDescription("");
		}
		
		if(projectFormBacker.getDescription().length() > PROJECT_DESCRIPTION_MAX_LENGTH) {
			errors.rejectValue("description", "", "Specified description is too long.");
		}
		
		// validate 'startDate' and 'endDate' fields //
		Date startDate = projectFormBacker.getRawStartDate();
		if(startDate == null) {
			errors.rejectValue("startDate", "", "Start date is required.");
		}
		
		Date endDate = projectFormBacker.getRawEndDate();
		if(endDate == null) {
			errors.rejectValue("endDate", "", "End date is required.");
		}
		
		if((startDate != null) && (endDate != null) && (endDate.equals(startDate) || endDate.before(startDate))) {
			errors.rejectValue("endDate", "", "End date must be after start date.");
		}
		
		// validate 'status' field //
		if(projectFormBacker.getStatus() == null) {
			projectFormBacker.setStatus(ProjectStatus.UNKNOWN);
		}
		
		if(projectFormBacker.getStatus() == ProjectStatus.UNKNOWN) {
			errors.rejectValue("status", "", "Status is required.");
		}
		*/
	}

	public ProjectValidator getProjectValidator() {
		return projectValidator;
	}
	public void setProjectValidator(ProjectValidator projectValidator) {
		this.projectValidator = projectValidator;
	}
}
