/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueValidator class.
 *     
 */
package ca.sciencestudio.model.facility.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 *
 */
public class InstrumentTechniqueValidator extends AbstractModelValidator<InstrumentTechnique> {

	@Override
	public void validate(InstrumentTechnique t, Errors errors) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected Class<InstrumentTechnique> getModelClass() {
		return InstrumentTechnique.class;
	}
}
