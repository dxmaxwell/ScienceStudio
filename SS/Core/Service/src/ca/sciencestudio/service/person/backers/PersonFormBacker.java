/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      PersonFormBacker class.
 *     
 */
package ca.sciencestudio.service.person.backers;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public class PersonFormBacker extends Person {
	
	private static final long serialVersionUID = 1L;

	public PersonFormBacker() {
		super();
	}
	
	public PersonFormBacker(Person person) {
		super(person);
	}
}
