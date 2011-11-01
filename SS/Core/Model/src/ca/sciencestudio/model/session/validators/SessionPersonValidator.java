/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonValidator class.
 *     
 */
package ca.sciencestudio.model.session.validators;

import java.util.Arrays;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.SessionPerson.Role;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.AbstractModelValidator;

/**
 * @author maxweld
 * 
 *
 */
public class SessionPersonValidator extends AbstractModelValidator<SessionPerson> {

	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PERSON_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_SESSION_GID = GID.DEFAULT_GID;
	public static final Role DEFAULT_ROLE = null;
	
	@Override
	public void validate(SessionPerson sessionPerson, Errors errors) {
		
		errors.pushNestedPath("personGid");
		GID gid = GID.parse(sessionPerson.getPersonGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Person GID field is required.");
		}
		else if(!gid.isType(Person.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Person GID field must have type: " + Person.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Person GID field must have a facility specifed.");
		}
		errors.popNestedPath();
		
		errors.pushNestedPath("sessionGid");
		gid = GID.parse(sessionPerson.getSessionGid());
		if(gid == null) {
			errors.rejectValue(null, EC_REQUIRED, "Session GID field is required.");
		}
		else if(!gid.isType(Session.GID_TYPE)) {
			errors.rejectValue(null, EC_INVALID, "Session GID field must have type: " + Session.GID_TYPE);
		}
		else if(gid.isLocal()) {
			errors.rejectValue(null, EC_INVALID, "Session GID field must have a facility specifed.");
		}	
		errors.popNestedPath();
		
		errors.pushNestedPath("role");
		if(sessionPerson.getRole() == null) {
			errors.rejectValue(null, EC_REQUIRED, "Role field not in list: " + Arrays.toString(SessionPerson.Role.values()));
		}
		errors.popNestedPath();
	}

	@Override
	protected Class<SessionPerson> getModelClass() {
		return SessionPerson.class;
	}
}
