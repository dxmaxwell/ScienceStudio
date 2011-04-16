/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonDAO interface.
 *     
 */
package ca.sciencestudio.model.person.dao;

import java.util.List;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 */
public interface PersonDAO {
	public Person createPerson();

	public int addPerson(Person person);
	public void editPerson(Person person);
	public void removePerson(int personId);
	public void removePerson(Person person);
	
	public Person getPersonById(int personId);
    public Person getPersonByUid(String personUid);
    
    public List<Person> getPersonList();
}
