/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class ExperimentValidator extends AbstractModelValidator<Experiment> {

	@Override
	public void validate(Experiment t, Errors errors) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Class<Experiment> getModelClass() {
		return Experiment.class;
	}
}
