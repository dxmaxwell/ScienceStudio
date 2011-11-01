/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestPersonDAO class.
 *     
 */
package ca.sciencestudio.model.person.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.person.dao.rest.support.RestPerson;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class RestPersonAuthzDAO extends AbstractRestModelAuthzDAO<Person> implements PersonAuthzDAO {

	public static final String PERSON_MODEL_PATH = "/persons";
	
	@Override
	public Data<Person> getByUsername(String username, String facility) {
		Person t;
		try {
			t = getRestTemplate().getForObject(getRestUrl("/whois", "username={username}", "facility={facility}"), getModelClass(), username, facility);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return new SimpleData<Person>((Person)null);
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model: " + e.getMessage());
			return new SimpleData<Person>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Person with Username: " + username + ", and facility: " + facility);
		}
		return new SimpleData<Person>(t);
	}
	
	@Override
	public Data<List<Person>> getAllByName(String user, String name) {
		List<Person> persons;
		try {
			persons = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "user={user}", "name={name}"), getModelArrayClass(), user, name));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Person>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Search all Persons by Name: " + name + " , size: " + persons.size());
		}
		return new SimpleData<List<Person>>(Collections.unmodifiableList(persons));
	}

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
	protected String getModelPath() {
		return PERSON_MODEL_PATH;
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
