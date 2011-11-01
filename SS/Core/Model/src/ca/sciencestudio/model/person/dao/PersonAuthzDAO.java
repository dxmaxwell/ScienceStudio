/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.person.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 * 
 *
 */
public interface PersonAuthzDAO extends ModelAuthzDAO<Person> {

	public Data<Person> getByUsername(String username, String facility);
	public Data<List<Person>> getAllByName(String user, String name);
}
