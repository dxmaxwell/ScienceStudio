/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractLaboratoryAdminController class.
 *     
 */
package ca.sciencestudio.nanofab.admin.service.controllers;

import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

/**
 * 
 * @author maxweld
 *
 */
public abstract class AbstractLaboratoryAdminController {

protected static final String FACILITY_ADMIN_NANOFAB = Authorities.getFacilityAuthority("ADMIN_NANOFAB");
	
	protected String facility;
	
	protected FacilityAuthzDAO facilityAuthzDAO;
	
	protected boolean canAdminLaboratory() {
		
		String user = SecurityUtil.getPersonGid();
		
		Authorities authorities = facilityAuthzDAO.getAuthorities(user, facility).get();
		if(authorities == null) {
			return false;
		}
		
		return authorities.containsAny(FACILITY_ADMIN_NANOFAB);
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
