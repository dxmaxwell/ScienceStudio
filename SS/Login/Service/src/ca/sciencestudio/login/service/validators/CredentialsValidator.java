/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      Credentials form backer validator class.
 *     
 */
package ca.sciencestudio.login.service.validators;

/**
 * @author maxweld
 * 
 */
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import ca.sciencestudio.login.service.backers.Credentials;

public class CredentialsValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "login.username.empty", "Username Empty or Whitespace");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "domain", "login.domain.empty", "Domain Empty or Whitespace");
	}
}
