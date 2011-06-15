/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingPersonDAO class.
 *     
 */
package ca.sciencestudio.model.dao.delegating;

import java.util.Collection;

import ca.sciencestudio.model.Person;
import ca.sciencestudio.model.dao.PersonDAO;

/**
 * @author maxweld
 * 
 *
 */
public class DelegatingPersonDAO extends AbstractDelegratingModelDAO<Person, PersonDAO> implements PersonDAO {

	public Collection<PersonDAO> getPersonDAOs() {
		return getModelDAOs();
	}
	public void setPersonDAOs(Collection<PersonDAO> personDAOs) {
		setModelDAOs(personDAOs);
	}
}
