/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentValidator class.
 *     
 */
package ca.sciencestudio.service.session.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ca.sciencestudio.service.session.backers.ExperimentFormBacker;

/**
 * @author maxweld
 *
 */
public class ExperimentFormBackerValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return ExperimentFormBacker.class.isAssignableFrom(clazz);
	}

	public void validate(Object object, Errors errors) {
		ExperimentFormBacker backer = (ExperimentFormBacker) object;
		if(backer == null) {
			errors.reject("", "Experiment form backer is null.");
			return;
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Name is required.");
		
		if(backer.getDescription() == null) {
			backer.setDescription("");
		}
		
		if(backer.getSampleId() == 0) {
			errors.rejectValue("sampleId", "", "Sample is required.");
		}
		
		if(backer.getInstrumentId() == 0) {
			errors.rejectValue("instrumentId", "", "Instrument is required.");
		}
		
		if(backer.getInstrumentTechniqueId() == 0) {
			errors.rejectValue("instrumentTechniqueId", "", "Technique is required.");
		}
	}
}
