/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentValidator class.
 *     
 */
package ca.sciencestudio.model.facility.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class InstrumentValidator extends AbstractModelValidator<Instrument> {

	@Override
	public void validate(Instrument instrument, Errors errors) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Class<Instrument> getModelClass() {
		return Instrument.class;
	}
}
