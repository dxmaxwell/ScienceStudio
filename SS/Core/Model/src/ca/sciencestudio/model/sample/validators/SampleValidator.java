/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleValidator class.
 *     
 */
package ca.sciencestudio.model.sample.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 *
 */
public class SampleValidator extends AbstractModelValidator<Sample> {

	@Override
	public void validate(Sample sample, Errors errors) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected Class<Sample> getModelClass() {
		return Sample.class;
	}
}
