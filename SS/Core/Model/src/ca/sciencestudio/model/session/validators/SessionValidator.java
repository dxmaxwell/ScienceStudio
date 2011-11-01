/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import java.util.Date;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class SessionValidator extends AbstractModelValidator<Session> {

	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 80;
	public static final int MAX_LENGTH_DESCRIPTION = 2000;
	public static final int MAX_LENGTH_PROPOSAL = 100;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PROJECT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_LABORATORY_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_PROPOSAL = "";
	public static final Date DEFAULT_START_DATE = null;
	public static final Date DEFAULT_END_DATE = null;
	
	@Override
	public void validate(Session session, Errors errors) {
		
		errors.pushNestedPath("projectGid");
		GID gid = GID.parse(session.getProjectGid());
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
		
		errors.pushNestedPath("laboratoryGid");
		gid = GID.parse(session.getLaboratoryGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Laboratory GID field is required.");
		}
		else if(!gid.isType(Laboratory.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Laboratory GID field must have type: " + Laboratory.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Laboratory GID field must have a facility specifed.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("name");
		if(session.getName() == null) {
			session.setName(DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespce.");
		rejectIfExceedsLength(MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("description");
		if(session.getDescription() == null) {
			session.setDescription(DEFAULT_DESCRIPTION);
		}
		rejectIfExceedsLength(MAX_LENGTH_DESCRIPTION, errors, null, EC_MAX_LENGTH, "Description field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("proposal");
		if(session.getDescription() == null) {
			session.setDescription(DEFAULT_PROPOSAL);
		}
		rejectIfExceedsLength(MAX_LENGTH_PROPOSAL, errors, null, EC_MAX_LENGTH, "Proposal field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("startDate");
		if(session.getStartDate() == DEFAULT_START_DATE) { 
			errors.rejectValue(null, EC_REQUIRED, "Start date field is unspecified.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("endDate");
		if(session.getEndDate() == DEFAULT_END_DATE) {
			errors.rejectValue(null, EC_REQUIRED, "End date field is unspecified.");
		}
		errors.popNestedPath();
	
		if(!errors.hasFieldErrors("startDate") && !errors.hasFieldErrors("endDate") && !session.getStartDate().before(session.getEndDate())) {
			errors.rejectValue("endDate", EC_AFTER, "End Date field is equal or before Start Date field.");
		}
	}

	@Override
	protected Class<Session> getModelClass() {
		return Session.class;
	}
}
