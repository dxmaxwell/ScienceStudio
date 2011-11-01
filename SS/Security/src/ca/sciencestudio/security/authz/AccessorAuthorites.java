package ca.sciencestudio.security.authz;

/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AccessorAuthorites class.
 *     
 */
import java.util.Collection;

import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 * 
 *
 */
public class AccessorAuthorites extends Authorities {
	
	private static final long serialVersionUID = 1L;

	public AccessorAuthorites() {
		super();
	}

	public AccessorAuthorites(Collection<? extends String> c) {
		super(c);
	}

	public boolean addSessionAuthority(SessionPerson.Role role) {
		return addSessionAuthority(role.name());
	}
	
	public boolean addProjectAuthority(ProjectPerson.Role role) {
		return addProjectAuthority(role.name());
	}
	
	public boolean addFacilityAuthority(LoginRole role) {
		return addFacilityAuthority(role.getName());
	}
}
