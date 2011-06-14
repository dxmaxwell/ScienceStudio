/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestPersonDAO class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import ca.sciencestudio.model.Person;
import ca.sciencestudio.model.dao.PersonDAO;
import ca.sciencestudio.model.dao.rest.support.AbstractRestModelDAO;
import ca.sciencestudio.model.dao.rest.support.RestPerson;

/**
 * @author maxweld
 * 
 *
 */
public class RestPersonDAO extends AbstractRestModelDAO<Person, RestPerson> implements PersonDAO {

	@Override
	protected RestPerson toRestModel(Person person) {
		RestPerson restPerson = new RestPerson();
		restPerson.setTitle(person.getTitle());
		restPerson.setFirstName(person.getFirstName());
		restPerson.setMiddleName(person.getMiddleName());
		restPerson.setLastName(person.getLastName());
		restPerson.setPhoneNumber(person.getPhoneNumber());
		restPerson.setMobileNumber(person.getMobileNumber());
		restPerson.setEmailAddress(person.getEmailAddress());
		restPerson.setModificationDate(person.getModificationDate());
		return restPerson;
	}

	@Override
	protected String getModelUrl() {
		return getBaseUrl() + "/persons";
	}

	@Override
	protected Class<Person> getModelClass() {
		return Person.class;
	}

	@Override
	protected Class<Person[]> getModelArrayClass() {
		return Person[].class;
	}
}
