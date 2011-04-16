/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      LaboratorySessionValidator class
 *
 */
package ca.sciencestudio.vespers.service.admin.validators;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.vespers.service.admin.backers.LaboratorySessionBacker;

/**
 * @author maxweld
 *
 */
public class LaboratorySessionValidator implements Validator {

	private static final int PROPOSAL_FIELD_MAX_LENGTH = 100;

	public boolean supports(Class<?> clazz) {
		return LaboratorySessionBacker.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		if(!(obj instanceof LaboratorySessionBacker)) {
			errors.reject("", "Laboratory session is null.");
			return;
		}
		
		LaboratorySessionBacker laboratorySession = (LaboratorySessionBacker) obj;
		
		String proposal = laboratorySession.getProposal();
		if(proposal == null) {
			laboratorySession.setProposal("");
		}
		else if(proposal.length() > PROPOSAL_FIELD_MAX_LENGTH) {
			errors.rejectValue("proposal", "proposal.length", "Proposal is too long.");
		}
		
		Date startDateTime = laboratorySession.getRawStartDate();
		if(startDateTime == null) {
			errors.rejectValue("startDate", "startdate.format", "Start date invalid format.");
			errors.rejectValue("startTime", "starttime.format", "Start time invalid format.");
		}
	
		Date endDateTime = laboratorySession.getRawEndDate();
		if(endDateTime == null) {
			errors.rejectValue("endDate", "enddate.format", "End date invalid format.");
			errors.rejectValue("endTime", "endtime.format", "End time invalid format.");
		}
		
		if((startDateTime != null) && (endDateTime != null)) {
			if(startDateTime.equals(endDateTime) || startDateTime.after(endDateTime)) {
				errors.rejectValue("endDate", "enddate.invalid", "End date must be after start date.");
				errors.rejectValue("endTime", "endtime.invalid", "End date must be after start date.");
			}
		}
	}
}
