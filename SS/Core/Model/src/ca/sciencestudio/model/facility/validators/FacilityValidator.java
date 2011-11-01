/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityValidator class.
 *     
 */
package ca.sciencestudio.model.facility.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class FacilityValidator extends AbstractModelValidator<Facility> {

	@Override
	public void validate(Facility t, Errors errors) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected Class<Facility> getModelClass() {
		return Facility.class;
	}
}
