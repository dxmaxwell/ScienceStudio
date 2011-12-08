/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractBeamlineAdminController class.
 *     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractBeamlineAdminController {

	protected static final String FACILITY_ADMIN_VESPERS = Authorities.getFacilityAuthority("ADMIN_VESPERS");
	
	protected String facility;
	
	protected FacilityAuthzDAO facilityAuthzDAO;
	
	protected boolean canAdminBeamline() {
		
		String user = SecurityUtil.getPersonGid();
		
		Authorities authorities = facilityAuthzDAO.getAuthorities(user, facility).get();
		if(authorities == null) {
			return false;
		}
		
		return authorities.containsAny(FACILITY_ADMIN_VESPERS);
	}

	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}

	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}
}
