/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import java.net.URI;
import java.util.Date;
import java.util.Map;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.AbstractModelValidator;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class ScanValidator extends AbstractModelValidator<Scan> {

	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_DATE_URL = 1000;
	public static final int MAX_LENGTH_PARAMETERS = 10000;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_EXPERIMENT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DATE_URL= "";
	public static final Parameters DEFAULT_PARAMETERS = new Parameters();
	public static final Date DEFAULT_START_DATE = null;
	public static final Date DEFAULT_END_DATE = null;
	
	@Override
	protected Class<Scan> getModelClass() {
		return Scan.class;
	}

	@Override
	public void validate(Scan scan, Errors errors) {
		
		errors.pushNestedPath("experimentGid");
		GID gid = GID.parse(scan.getExperimentGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Experiment GID field is required.");
		}
		else if(!gid.isType(Experiment.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Experiment GID field must have type: " + Experiment.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Experiment GID field must have a facility specifed.");
		}	
		errors.popNestedPath();
		
		errors.pushNestedPath("name");
		if(scan.getName() == null) {
			scan.setName(DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespace.");
		rejectIfExceedsLength(MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("dataUrl");
		if(scan.getDataUrl() == null) {
			scan.setDataUrl(DEFAULT_DATE_URL);
		}
		try {
			URI.create(scan.getDataUrl());
		} 
		catch(IllegalArgumentException e) {
			errors.rejectValue(null, EC_INVALID, "Data URL field contains invalid URI syntax.");
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Date URL field is empty or whitespace.");		
		rejectIfExceedsLength(MAX_LENGTH_DATE_URL, errors, null, EC_MAX_LENGTH, "Data URL field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("parameters");
		if(scan.getParameters() == null) {
			scan.setParameters(new Parameters(DEFAULT_PARAMETERS));
		}
		// The value of this check is only minimal. The parameters may not get converted to JSON. //
		if(estimateParametersLength(scan.getParameters()) > MAX_LENGTH_PARAMETERS) {
			errors.rejectValue(null, EC_MAX_LENGTH, "Parameters field exceeds maximum length.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("startDate");
		if(scan.getStartDate() == null) { 
			errors.rejectValue(null, EC_REQUIRED, "Start Date field is required.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("endDate");
		if(scan.getEndDate() == null) {
			errors.rejectValue(null, EC_REQUIRED, "End Date field is required.");
		}
		errors.popNestedPath();
		
		if(!errors.hasFieldErrors("startDate") && !errors.hasFieldErrors("endDate") && !scan.getStartDate().before(scan.getEndDate())) {
			errors.rejectValue("endDate", EC_AFTER, "End Date field before or equal to Start Date.");
		}
	}
	
	// Attempt to estimate the length of the parameters as JSON. //
	protected int estimateParametersLength(Parameters parameters) {
		int length = 0;
		for(Map.Entry<String,String> entry : parameters.entrySet()) {
			length += entry.getKey().length() + entry.getValue().length() + 6;
		}
		return length + 4;
	}
}
