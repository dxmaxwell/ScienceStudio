/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    DummySecurityStrategy abstract class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Date;

import ca.sciencestudio.model.Person;

/**
 * @author maxweld
 *
 */
public abstract class DummySecurityStrategy implements SecurityStrategy {

	private static final Person UNKNOWN_PERSON = buildUnknownPerson();
	
	@Override
	public String getUsername() {
		return "UnknownUsername";
	}
	
	@Override
	public Person getPerson() {
		return UNKNOWN_PERSON;
	}
	
	protected static Person buildUnknownPerson() {
		Person person = new Person();
		person.setFirstName("unknown");
		person.setLastName("unknown");
		person.setPhoneNumber("555-555-1234");
		person.setMobileNumber("555-555-4321");
		person.setEmailAddress("unknown@fake.za");
		person.setModificationDate(new Date());
		return person;
	}
}
