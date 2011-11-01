/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityValidator class.
 *     
 */
package ca.sciencestudio.model.facility.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class LaboratoryValidator extends AbstractModelValidator<Laboratory> {

	@Override
	public void validate(Laboratory laboratory, Errors errors) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Class<Laboratory> getModelClass() {
		return Laboratory.class;
	}
}
