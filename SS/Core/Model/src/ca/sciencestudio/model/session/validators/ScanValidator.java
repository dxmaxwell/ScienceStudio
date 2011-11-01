/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import java.net.URI;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class ScanValidator extends AbstractModelValidator<Scan> {

	@Override
	protected Class<Scan> getModelClass() {
		return Scan.class;
	}

	@Override
	public void validate(Scan scan, Errors errors) {
	
		errors.pushNestedPath("name");
		if(scan.getName() == null) {
			scan.setName(Scan.DEFAULT_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Name field is empty or whitespace.");
		rejectIfExceedsLength(Scan.MAX_LENGTH_NAME, errors, null, EC_MAX_LENGTH, "Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("dataUrl");
		if(scan.getDataUrl() == null) {
			scan.setDataUrl(Scan.DEFAULT_DATE_URL);
		}
		try {
			URI.create(scan.getDataUrl());
		} 
		catch(IllegalArgumentException e) {
			errors.rejectValue(null, EC_INVALID, "Data URL field contains invalid URI syntax.");
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Date URL field is empty or whitespace.");		
		rejectIfExceedsLength(Scan.MAX_LENGTH_DATE_URL, errors, null, EC_MAX_LENGTH, "Data URL field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("parameters");
		if(scan.getParameters() == null) {
			scan.setParameters(Scan.DEFAULT_PARAMETERS);
		}
		rejectIfExceedsLength(Scan.MAX_LENGTH_PARAMETERS, errors, null, EC_MAX_LENGTH, "Parameters field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("startDate");
		if(scan.getStartDate() == null) { 
			scan.setStartDate(Scan.DEFAULT_START_DATE);
		} 	
		if(scan.getStartDate().equals(Scan.DEFAULT_START_DATE)) {
			errors.rejectValue(null, EC_REQUIRED, "Start Date field equals default.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("endDate");
		if(scan.getEndDate() == null) {
			scan.setEndDate(Scan.DEFAULT_END_DATE);
		}
		if(scan.getEndDate().equals(Scan.DEFAULT_END_DATE)) {
			errors.rejectValue(null, EC_REQUIRED, "End Date field equals default.");
		}
		errors.popNestedPath();
		
		if(!errors.hasFieldErrors("startDate") && !errors.hasFieldErrors("endDate") && !scan.getStartDate().before(scan.getEndDate())) {
			errors.rejectValue("endDate", EC_AFTER, "End Date field equal or after Start Date.");
		}
		
//		errors.pushNestedPath("experimentId");
//		if(scan.getExperimentId() <= 0) {
//			errors.rejectValue(null, EC_INVALID, "Experiment Id field less than or equal to zero.");
//		}
//		errors.popNestedPath();
	}
}
