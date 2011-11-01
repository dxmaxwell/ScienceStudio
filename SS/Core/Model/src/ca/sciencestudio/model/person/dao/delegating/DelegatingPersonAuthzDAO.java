/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingPersonAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.person.dao.delegating;

import java.util.Collection;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.person.dao.PersonBasicDAO;
import ca.sciencestudio.model.dao.delegating.AbstractDelegratingModelAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class DelegatingPersonAuthzDAO extends AbstractDelegratingModelAuthzDAO<Person, PersonAuthzDAO> implements PersonAuthzDAO {

	public Collection<PersonAuthzDAO> getPersonDAOs() {
		return getModelDAOs();
	}
	public void setPersonDAOs(Collection<PersonAuthzDAO> personDAOs) {
		setModelDAOs(personDAOs);
	}
}
