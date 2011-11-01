/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class SessionValidator extends AbstractModelValidator<Session> {

	@Override
	public void validate(Session t, Errors errors) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected Class<Session> getModelClass() {
		return Session.class;
	}
}
