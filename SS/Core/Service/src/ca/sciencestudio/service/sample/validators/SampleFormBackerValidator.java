/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleValidator class.
 *     
 */
package ca.sciencestudio.service.sample.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ca.sciencestudio.model.sample.SampleState;
import ca.sciencestudio.service.sample.backers.SampleFormBacker;

/**
 * @author maxweld
 *
 */
public class SampleFormBackerValidator implements Validator {

	private static final int SAMPLE_NAME_MAX_LENGTH = 40;
	private static final int SAMPLE_QUANTITY_MAX_LENGTH = 100;
	private static final int SAMPLE_CAS_NUMBER_MAX_LENGTH = 45;
	private static final int SAMPLE_DESCRIPTION_MAX_LENGTH = 255;
	private static final int SAMPLE_OTHER_HAZARDS_MAX_LENGTH = 255;
	
	public boolean supports(Class<?> theClass) {
		return SampleFormBacker.class.isAssignableFrom(theClass);
	}

	public void validate(Object object, Errors errors) {
		SampleFormBacker sampleBacker = (SampleFormBacker) object;

		if(sampleBacker.getName() == null) {
			sampleBacker.setName("");
		}
		
		if(sampleBacker.getName().length() > SAMPLE_NAME_MAX_LENGTH) {
			errors.rejectValue("name", "", "Name exceeds maximum length");
		}
		else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Name is required.");
		}
		
		
		if(sampleBacker.getDescription() == null) {
			sampleBacker.setDescription("");
		}
		else if(sampleBacker.getDescription().length() >= SAMPLE_DESCRIPTION_MAX_LENGTH) {
			errors.rejectValue("description", "", "Description exceeds maximum length.");
		}
		
		if(sampleBacker.getCasNumber() == null) {
			sampleBacker.setCasNumber("");
		}
		else if(sampleBacker.getCasNumber().length() > SAMPLE_CAS_NUMBER_MAX_LENGTH) {
			errors.rejectValue("casNumber", "", "CAS Number exceeds maximum length.");
		}
		
		if(sampleBacker.getState() == null) {
			sampleBacker.setState(SampleState.UNKNOWN);
		}
		
		if(errors.hasFieldErrors("state") || (sampleBacker.getState() == SampleState.UNKNOWN)) {
			errors.rejectValue("state", "", "Please select a state.");
		}
		
		if(sampleBacker.getQuantity() == null) {
			sampleBacker.setQuantity("");
		}
		
		if(sampleBacker.getQuantity().length() > SAMPLE_QUANTITY_MAX_LENGTH) {
			errors.rejectValue("quantity", "", "Quantity exceeds maximum length.");
		}
		else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "", "Quantity is required.");
		}
		
		if(sampleBacker.getOtherHazards() == null) {
			sampleBacker.setOtherHazards("");
		}
		
		if(sampleBacker.isOther()) {
			if(sampleBacker.getOtherHazards().length() > SAMPLE_OTHER_HAZARDS_MAX_LENGTH) {
				errors.rejectValue("otherHazards", "", "Other Hazards exceeds the maximum length.");
			}
			else {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "otherHazards", "", "Please describe other hazards.");
			}
		}
	}
}
