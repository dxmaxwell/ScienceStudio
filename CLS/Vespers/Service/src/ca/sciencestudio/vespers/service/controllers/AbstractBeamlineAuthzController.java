/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractBeamlineAuthzController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.vespers.device.proxy.event.BeamlineSessionProxyEventListener;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractBeamlineAuthzController {

	protected static final String FACILITY_ADMIN_VESPERS = Authorities.getFacilityAuthority("ADMIN_VESPERS");
	
	protected static final String SESSION_OBSERVER = Authorities.getSessionAuthority(SessionPerson.Role.OBSERVER.name());
	protected static final String SESSION_EXPERIMENTER = Authorities.getSessionAuthority(SessionPerson.Role.EXPERIMENTER.name());
	
	protected static final String VALUE_KEY_SESSION_GID = "sessionGid";
	protected static final String VALUE_KEY_CONTROLLER_GID = "controllerGid";
	
	protected SessionAuthzDAO sessionAuthzDAO;
	
	protected BeamlineSessionProxyEventListener beamlineSessionProxy;
	
	protected Authorities getAuthorities() {
		String sessionGid = (String) beamlineSessionProxy.get(VALUE_KEY_SESSION_GID);
		if(sessionGid == null) {
			return null;
		}	
		return sessionAuthzDAO.getAuthorities(SecurityUtil.getPersonGid(), sessionGid).get();
	}
	
	protected boolean canReadBeamline() {		
		Authorities authorities = getAuthorities();
		if(authorities == null) {
			return false;
		}
		return (authorities.containsSessionAuthority() || authorities.containsAny(FACILITY_ADMIN_VESPERS));
	}
	
	protected boolean canWriteBeamline() {
		String controllerGid = (String) beamlineSessionProxy.get(VALUE_KEY_CONTROLLER_GID);
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

	public BeamlineSessionProxyEventListener getBeamlineSessionProxy() {
		return beamlineSessionProxy;
	}
	public void setBeamlineSessionProxy(BeamlineSessionProxyEventListener beamlineSessionProxy) {
		this.beamlineSessionProxy = beamlineSessionProxy;
	}
}
