/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonGridBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.session.SessionPerson;

/**
 * @author maxweld
 *
 */
public class SessionPersonGridBacker extends SessionPersonFormBacker {

	private static final long serialVersionUID = 1L;

	public SessionPersonGridBacker(SessionPerson sessionPerson, Person person) {
		super(sessionPerson, person);
	}
}
