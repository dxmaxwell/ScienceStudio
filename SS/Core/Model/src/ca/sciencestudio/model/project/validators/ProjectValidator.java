/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectValidator class.
 *     
 */
package ca.sciencestudio.model.project.validators;

import java.util.Arrays;
import java.util.Date;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.Project.Status;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class ProjectValidator extends AbstractModelValidator<Project> {
	
	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 80;
	public static final int MAX_LENGTH_DESCRIPTION = 2000;
	public static final int MAX_LENGTH_STATUS = 20;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_FACILITY_GID = GID.DEFAULT_GID;
	public static final Date DEFAULT_START_DATE = null;
	public static final Date DEFAULT_END_DATE = null;
	public static final Status DEFAULT_STATUS = null;
	
	@Override
	protected Class<Project> getModelClass() {
		return Project.class;
	}

	@Override
	public void validate(Project project, Errors errors) {
		
		errors.pushNestedPath("name");
		if(project.getName() == null) {
			project.setName(DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespce.");
		rejectIfExceedsLength(MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("description");
		if(project.getDescription() == null) {
			project.setDescription(DEFAULT_DESCRIPTION);
		}
		rejectIfExceedsLength(MAX_LENGTH_DESCRIPTION, errors, null, EC_MAX_LENGTH, "Description field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("facilityGid");
		GID gid = GID.parse(project.getFacilityGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Facility GID field is required.");
		}
		else if(!gid.isType(Facility.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Facility GID field must have type: " + Facility.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Facility GID field must have a facility specifed.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("startDate");
		if(project.getStartDate() == DEFAULT_START_DATE) {
			errors.rejectValue(null, EC_REQUIRED, "Start date field is unspecified.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("endDate");
		if(project.getEndDate() == DEFAULT_END_DATE) {
			errors.rejectValue(null, EC_REQUIRED, "End date field is unspecified.");
		}
		errors.popNestedPath();
		
		if(!errors.hasFieldErrors("startDate") && !errors.hasFieldErrors("endDate") && !project.getStartDate().before(project.getEndDate())) {
			errors.rejectValue("endDate", EC_AFTER, "End Date field is equal or before Start Date field.");
		}
		
		errors.pushNestedPath("status");
		if(project.getStatus() == DEFAULT_STATUS) {
			errors.rejectValue(null, EC_REQUIRED, "Status field is not in list: " + Arrays.toString(Project.Status.values()));
		}
		else if(project.getStatus().name().length() > MAX_LENGTH_STATUS) {
			errors.rejectValue(null, EC_MAX_LENGTH, "Status field exceeds maximum length.");
		}
		errors.popNestedPath();
	}
}
