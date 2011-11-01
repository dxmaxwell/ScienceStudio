/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueValidator class.
 *     
 */
package ca.sciencestudio.model.facility.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class TechniqueValidator extends AbstractModelValidator<Technique> {

	@Override
	public void validate(Technique t, Errors errors) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Class<Technique> getModelClass() {
		return Technique.class;
	}
}
