/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelController class.
 *     
 */
package ca.sciencestudio.service.controllers;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelController {

	protected static final String ERROR_VIEW = "frag/error";
	
	protected static final String FACILITY_ADMIN_PROJECTS = Authorities.getFacilityAuthority("ADMIN_PROJECTS");
	protected static final String FACILITY_ADMIN_SESSIONS = Authorities.getFacilityAuthority("ADMIN_SESSIONS");
	
	protected static final String PROJECT_COLLABORATOR = Authorities.getProjectAuthority(ProjectPerson.Role.COLLABORATOR.name());
	protected static final String PROJECT_RESEARCHER = Authorities.getProjectAuthority(ProjectPerson.Role.RESEARCHER.name());

	protected static final String SESSION_OBSERVER = Authorities.getSessionAuthority(SessionPerson.Role.OBSERVER.name());
	protected static final String SESSION_EXPERIMENTER = Authorities.getSessionAuthority(SessionPerson.Role.EXPERIMENTER.name());

	protected static Authorities mergeAuthorities(Authorities projectAuthorities, Authorities sessionAuthorities) {
		
		Authorities authorities = new Authorities();
		
		if(projectAuthorities.contains(FACILITY_ADMIN_PROJECTS)) {
			authorities.add(FACILITY_ADMIN_PROJECTS);
		}
		
		if(sessionAuthorities.contains(FACILITY_ADMIN_SESSIONS)) {
			authorities.add(FACILITY_ADMIN_SESSIONS);
		}
		
		authorities.addAll(projectAuthorities.getProjectAuthorities());
		authorities.addAll(sessionAuthorities.getSessionAuthorities());
		return authorities;
	}
}
