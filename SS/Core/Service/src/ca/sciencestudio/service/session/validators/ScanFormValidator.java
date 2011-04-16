/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFormValidator class.
 *     
 */
package ca.sciencestudio.service.session.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ca.sciencestudio.service.session.backers.ScanFormBacker;

/**
 * @author maxweld
 *
 */
public class ScanFormValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return ScanFormBacker.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object object, Errors errors) {
		ScanFormBacker scanFormBacker = (ScanFormBacker) object;
		if(scanFormBacker == null) {
			errors.reject("scan.null", "Scan is required.");
			return;
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Scan name is required.");
	}
}
