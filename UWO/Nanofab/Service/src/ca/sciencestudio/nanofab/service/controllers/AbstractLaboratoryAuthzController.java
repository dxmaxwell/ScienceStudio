/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractLaboratoryAuthzController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractLaboratoryAuthzController {
	
	protected static final String FACILITY_ADMIN_NANOFAB = Authorities.getFacilityAuthority("ADMIN_NANOFAB");
	
	protected static final String SESSION_OBSERVER = Authorities.getSessionAuthority(SessionPerson.Role.OBSERVER.name());
	protected static final String SESSION_EXPERIMENTER = Authorities.getSessionAuthority(SessionPerson.Role.EXPERIMENTER.name());
	
	protected SessionAuthzDAO sessionAuthzDAO;
	
	protected NanofabSessionStateMap nanofabSessionStateMap;
	
	protected Authorities getAuthorities() {
		String sessionGid = nanofabSessionStateMap.getSessionGid();
		if(sessionGid == null) {
			return null;
		}	
		return sessionAuthzDAO.getAuthorities(SecurityUtil.getPersonGid(), sessionGid).get();
	}
	
	protected boolean canReadLaboratory() {		
		Authorities authorities = getAuthorities();
		if(authorities == null) {
			return false;
		}
		return (authorities.containsSessionAuthority() || authorities.containsAny(FACILITY_ADMIN_NANOFAB));
	}
	
	protected boolean canWriteLaboratory() {
		String controllerGid = nanofabSessionStateMap.getControllerGid();
		if(controllerGid == null) {
			return false;
		}
		return (controllerGid.equalsIgnoreCase(SecurityUtil.getPersonGid()));		
	}
	
	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
}
