/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisPersonDAO implementation.
 *     
 */
package ca.sciencestudio.model.person.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonDAO;
import ca.sciencestudio.model.person.ibatis.IbatisPerson;

/**
 * @author maxweld
 */
public class IbatisPersonDAO extends SqlMapClientDaoSupport implements PersonDAO {
	
	@Override
	public Person createPerson() {
		return new IbatisPerson();
	}

	@Override
	public int addPerson(Person person) {
		Integer id = (Integer)getSqlMapClientTemplate().insert("addPerson", person);
		logger.info("Add person with id: " + id);
		return id;
	}
	
	@Override
	public void editPerson(Person person) {
		int rowsAffected = getSqlMapClientTemplate().update("editPerson", person);
		logger.info("Edit person, rows afftected: " + rowsAffected);
		return;
	}
	
	@Override
	public void removePerson(int personId) {
		int rowsAffected = getSqlMapClientTemplate().delete("removePerson", personId);
		logger.info("Remove Person with id: " + personId + ", rows affected: " + rowsAffected);
		return;
	}
	
	@Override
	public void removePerson(Person person) {
		int rowsAffected = getSqlMapClientTemplate().delete("removePerson", person.getId());
		logger.info("Remove person with id: " + person.getId() + ", rows affected: " + rowsAffected);
		return;
	}
	
	@Override
	public Person getPersonById(int personId) { 
		Person person = (Person) getSqlMapClientTemplate().queryForObject("getPersonById", new Integer(personId));
		logger.info("Get Person with id: " + personId);
		return person;
	}
	
	@Override
	public Person getPersonByUid(String personUid) {
		Person person = (Person) getSqlMapClientTemplate().queryForObject("getPersonByUid", personUid);
		logger.info("Get person with uid: " + personUid);
		return person;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Person> getPersonList() {
		List<Person> list = getSqlMapClientTemplate().queryForList("getPersonList");
		logger.info("Get person list, size: " + list.size());
		return list;
	}	
}
