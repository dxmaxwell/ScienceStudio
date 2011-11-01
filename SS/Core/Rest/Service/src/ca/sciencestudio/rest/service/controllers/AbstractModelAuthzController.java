/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelAuthzController abstract class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractModelAuthzController<T extends Model> extends AbstractModelController<T> {

	protected static final String FACILITY_ADMIN_PROJECTS = Authorities.getFacilityAuthority("ADMIN_PROJECTS");
	protected static final String FACILITY_ADMIN_SESSIONS = Authorities.getFacilityAuthority("ADMIN_SESSIONS");
	
	protected static final String PROJECT_OBSERVER = Authorities.getProjectAuthority(ProjectPerson.Role.COLLABORATOR.name());
	protected static final String PROJECT_RESEARCHER = Authorities.getProjectAuthority(ProjectPerson.Role.RESEARCHER.name());

	protected static final String SESSION_EXPERIMENTER = Authorities.getSessionAuthority(SessionPerson.Role.EXPERIMENTER.name());
	
}
