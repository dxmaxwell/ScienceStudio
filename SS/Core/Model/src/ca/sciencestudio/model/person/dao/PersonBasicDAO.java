/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.person.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 * 
 *
 */
public interface PersonBasicDAO extends ModelBasicDAO<Person> {

	public Person getByUsername(String username, String facility);
	public List<Person> getAllByName(String name);
}
