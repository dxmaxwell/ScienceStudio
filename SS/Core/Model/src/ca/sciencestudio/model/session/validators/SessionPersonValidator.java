/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class SessionPersonValidator extends AbstractModelValidator<SessionPerson> {

	@Override
	public void validate(SessionPerson sessionPerson, Errors errors) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Class<SessionPerson> getModelClass() {
		return SessionPerson.class;
	}
}
