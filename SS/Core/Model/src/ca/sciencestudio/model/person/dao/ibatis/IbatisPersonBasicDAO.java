/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisPersonBasicDAO class.
 *     
 */
package ca.sciencestudio.model.person.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonBasicDAO;
import ca.sciencestudio.model.person.dao.ibatis.support.IbatisPerson;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class IbatisPersonBasicDAO extends AbstractIbatisModelBasicDAO<Person> implements PersonBasicDAO {
	
	@Override
	public String getGidType() {
		return Person.GID_TYPE;
	}
	
	@Override
	public Person getByUsername(String username, String facility) {
		if(!getGidFacility().equals(facility)) {
			return null;
		}
		
		Person person;
		try {
			person = toModel(getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByUsername"), username));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Person: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Person with Username: " + username + ", and facility: " + facility);
		}
		return person;
	}
	
	@Override
	public List<Person> getAllByName(String name) {
		List<Person> persons;
		try {
			persons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByName"), name));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Search all Persons by Name: " + name + ", size: " + persons.size());
		}
		return Collections.unmodifiableList(persons);
	}

	@Override
	protected IbatisPerson toIbatisModel(Person person) {
		if(person == null) {
			return null;
		}
		IbatisPerson ibatisPerson = new IbatisPerson();
		GID gid = GID.parse(person.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisPerson.setId(gid.getId());
		}
		ibatisPerson.setTitle(person.getTitle());
		ibatisPerson.setFirstName(person.getFirstName());
		ibatisPerson.setMiddleName(person.getMiddleName());
		ibatisPerson.setLastName(person.getLastName());
		ibatisPerson.setPhoneNumber(person.getPhoneNumber());
		ibatisPerson.setMobileNumber(person.getMobileNumber());
		ibatisPerson.setEmailAddress(person.getEmailAddress());
		ibatisPerson.setModificationDate(person.getModificationDate());
		return ibatisPerson;
	}
	
	@Override
	protected Person toModel(Object obj) {
		if(!(obj instanceof IbatisPerson)) {
			return null;
		}
		IbatisPerson ibatisPerson = (IbatisPerson)obj;
		Person person = new Person();
		person.setGid(GID.format(getGidFacility(), ibatisPerson.getId(), getGidType()));
		person.setTitle(ibatisPerson.getTitle());
		person.setFirstName(ibatisPerson.getFirstName());
		person.setMiddleName(ibatisPerson.getMiddleName());
		person.setLastName(ibatisPerson.getLastName());
		person.setPhoneNumber(ibatisPerson.getPhoneNumber());
		person.setMobileNumber(ibatisPerson.getMobileNumber());
		person.setEmailAddress(ibatisPerson.getEmailAddress());
		person.setModificationDate(ibatisPerson.getModificationDate());
		return person;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Person" + suffix;
	}
}
