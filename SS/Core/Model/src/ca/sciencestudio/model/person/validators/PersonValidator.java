/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonValidator class.
 *     
 */
package ca.sciencestudio.model.person.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class PersonValidator extends AbstractModelValidator<Person> {

	@Override
	public void validate(Person person, Errors errors) {
		
		errors.pushNestedPath("title");
		if(person.getTitle() == null) {
			person.setTitle(Person.DEFAULT_TITLE);
		}
		rejectIfExceedsLength(Person.MAX_LENGTH_TITLE, errors, null, EC_MAX_LENGTH, "Title field exceeds maximum length.");
		errors.popNestedPath();

		errors.pushNestedPath("firstName"); // Required //
		if(person.getFirstName() == null) {
			person.setFirstName(Person.DEFAULT_FIRST_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "First Name field is required.");
		rejectIfExceedsLength(Person.MAX_LENGTH_FIRST_NAME, errors, null, EC_MAX_LENGTH, "First Name field exceeds maximum length.");		
		errors.popNestedPath();

		errors.pushNestedPath("middleName");
		if(person.getMiddleName() == null) {
			person.setMiddleName(Person.DEFAULT_MIDDLE_NAME);
		}
		rejectIfExceedsLength(Person.MAX_LENGTH_MIDDLE_NAME, errors, null, EC_MAX_LENGTH, "Middle Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("lastName"); // Required //
		if(person.getLastName() == null) {
			person.setLastName(Person.DEFAULT_LAST_NAME);
		}
		rejectIfEmptyOrWhitespace(errors, null, EC_REQUIRED, "Last Name field is required.");
		rejectIfExceedsLength(Person.MAX_LENGTH_LAST_NAME, errors, null, EC_MAX_LENGTH, "Last Name field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("phoneNumber");
		if(person.getPhoneNumber() == null) {
			person.setPhoneNumber(Person.DEFAULT_PHONE_NUMBER);
		}
		rejectIfExceedsLength(Person.MAX_LENGTH_PHONE_NUMBER, errors, null, EC_MAX_LENGTH, "Phone Number field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("mobileNumber");
		if(person.getMobileNumber() == null) {
			person.setMobileNumber(Person.DEFAULT_MOBILE_NUMBER);
		}
		rejectIfExceedsLength(Person.MAX_LENGTH_MOBILE_NUMBER, errors, null, EC_MAX_LENGTH, "Mobile Number field exceeds maximum length.");
		errors.popNestedPath();
		
		errors.pushNestedPath("emailAddress");
		if(person.getEmailAddress() == null) {
			person.setEmailAddress(Person.DEFAULT_EMAIL_ADDRESS);
		}
		if(person.getEmailAddress().length() > 0) {
			rejectIfInvalidEmail(errors, null, EC_INVALID, "Email Address field is invalid.");
		}
		rejectIfExceedsLength(Person.MAX_LENGTH_EMAIL_ADDRESS, errors, null, EC_MAX_LENGTH, "Email Address field exceeds maximum length.");
		errors.popNestedPath();
	}

	@Override
	protected Class<Person> getModelClass() {
		return Person.class;
	}	
}
