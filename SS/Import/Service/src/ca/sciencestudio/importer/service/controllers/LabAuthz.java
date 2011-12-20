/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LabAuthz class.
 *     
 */
package ca.sciencestudio.importer.service.controllers;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

public class LabAuthz {
	
	private String admin; 
	private Authorities authorities;
	
	private LabAuthz(SessionAuthzDAO sessionAuthzDAO, String user, String sessionGid, String labName) {
		this.authorities = sessionAuthzDAO.getAuthorities(user, sessionGid).get();
		this.admin = Authorities.getFacilityAuthority("ADMIN_" + labName.toUpperCase());
	}
	
	public static LabAuthz createInstance(String labName, String sessionGid, SessionAuthzDAO sessionAuthzDAO) {
		String user = SecurityUtil.getPersonGid(); 
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			return null;
		}	
		return new LabAuthz(sessionAuthzDAO, user, sessionGid, labName);
	}
	
	public Authorities getAuthorities() {
		return this.authorities;
	}
	
	public boolean canRead() {		
		return (authorities.containsSessionAuthority() || authorities.containsAny(this.admin));
	}
	
	public boolean canWrite() {
		return (authorities.containsAny(this.admin));		
	}
	
}
