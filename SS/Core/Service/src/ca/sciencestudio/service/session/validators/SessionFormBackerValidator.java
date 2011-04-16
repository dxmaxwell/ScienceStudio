/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionFormBackerValidator class.
 *     
 */
package ca.sciencestudio.service.session.validators;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ca.sciencestudio.service.session.backers.SessionFormBacker;

/**
 * @author maxweld
 *
 */
public class SessionFormBackerValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return SessionFormBacker.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object object, Errors errors) {
		SessionFormBacker backer = (SessionFormBacker) object;
		
		if(backer == null) {
			errors.reject("", "SessionFormBacker is required.");
			return;
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Name is required.");
		
		if(backer.getLaboratoryId() == 0) {
			errors.rejectValue("laboratoryId", "", "Laboratory is required.");
		}
		
		Date startDate = backer.getRawStartDate();
		if(startDate == null) {
			errors.rejectValue("startDate", "", "Start date is required.");
			errors.rejectValue("startTime", "", "Start time is required.");
		}
			
		Date endDate = backer.getRawEndDate();
		if(endDate == null) {
			errors.rejectValue("endDate", "", "End date is required.");
			errors.rejectValue("endTime", "", "End time is required.");
		}
			
		if((startDate != null) && (endDate != null) && (endDate.equals(startDate) || endDate.before(startDate))) {
			errors.rejectValue("endDate", "", "End date must be after start date.");
			errors.rejectValue("endTime", "", "End time must be after start time.");
		}
	}	
}
