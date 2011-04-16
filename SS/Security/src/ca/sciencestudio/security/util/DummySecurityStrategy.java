/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    DummySecurityStrategy abstract class.
 *     
 */
package ca.sciencestudio.security.util;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public abstract class DummySecurityStrategy implements SecurityStrategy {

	private static final Person UNKNOWN_PERSON = new UnknownPerson();
	
	@Override
	public String getUsername() {
		return "UnknownUsername";
	}
	
	@Override
	public Person getPerson() {
		return UNKNOWN_PERSON;
	}
}
