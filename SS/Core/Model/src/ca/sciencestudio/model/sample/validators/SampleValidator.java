/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleValidator class.
 *     
 */
package ca.sciencestudio.model.sample.validators;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.Sample.Hazard;
import ca.sciencestudio.model.sample.Sample.State;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class SampleValidator extends AbstractModelValidator<Sample> {

	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 80;
	public static final int MAX_LENGTH_DESCRIPTION = 2000;
	public static final int MAX_LENGTH_QUANTITY = 100;
	public static final int MAX_LENGTH_CAS_NUMBER = 45;
	public static final int MAX_LENGTH_HAZARDS = 1000;
	public static final int MAX_LENGTH_OTHER_HAZARDS = 1000;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PROJECT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final State DEFAULT_STATE = null;
	public static final String DEFAULT_QUANTITY = "";
	public static final String DEFAULT_CAS_NUMBER = "";
	public static final Set<Hazard> DEFAULT_HAZARDS = Collections.emptySet();
	public static final String DEFAULT_OTHER_HAZARDS = "";
	
	@Override
	public void validate(Sample sample, Errors errors) {
		
		errors.pushNestedPath("projectGid");
		GID gid = GID.parse(sample.getProjectGid());
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
		
		errors.pushNestedPath("name");
		if(sample.getName() == null) {
			sample.setName(DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespce.");
		rejectIfExceedsLength(MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("description");
		if(sample.getDescription() == null) {
			sample.setDescription(DEFAULT_DESCRIPTION);
		}
		rejectIfExceedsLength(MAX_LENGTH_DESCRIPTION, errors, null, EC_MAX_LENGTH, "Description field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("state");
		if(sample.getState() == null) {
			errors.rejectValue(null, EC_REQUIRED, "State field not in list: " + Arrays.toString(Sample.State.values()));
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("quantity");
		if(sample.getQuantity() == null) {
			sample.setQuantity(DEFAULT_QUANTITY);
		}
		rejectIfExceedsLength(MAX_LENGTH_QUANTITY, errors, null, EC_MAX_LENGTH, "Quantity field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("casNumber");
		if(sample.getCasNumber() == null) {
			sample.setCasNumber(DEFAULT_CAS_NUMBER);
		}
		rejectIfExceedsLength(MAX_LENGTH_CAS_NUMBER, errors, null, EC_MAX_LENGTH, "CAS Number field exceeds maximum length.");
		errors.popNestedPath();
		
		Set<Hazard> hazards = sample.getHazards();
		
		errors.pushNestedPath("hazards");
		int length = 0;
		for(Hazard hazard : hazards) {
			length += hazard.name().length() + 1;
		}
		if(length > MAX_LENGTH_HAZARDS) {
			errors.rejectValue(null, EC_MAX_LENGTH, "Hazards field exceeds maximum length.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("otherHazards");
		if(sample.getOtherHazards() == null) {
			sample.setOtherHazards(DEFAULT_OTHER_HAZARDS);
		}
		if(hazards.contains(Hazard.OTHER)) {
			rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Other Hazards field is empty or whitespce.");
			rejectIfExceedsLength(MAX_LENGTH_OTHER_HAZARDS, errors, null, EC_MAX_LENGTH, "Other Hazards field exceeds maximum length.");
		} else {
			sample.setOtherHazards(DEFAULT_OTHER_HAZARDS); // Clear content of field. //
		}
		errors.popNestedPath();
	}

	@Override
	protected Class<Sample> getModelClass() {
		return Sample.class;
	}
}
