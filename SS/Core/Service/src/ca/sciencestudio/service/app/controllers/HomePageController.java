/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	HomePageController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 *
 */
@Controller
public class HomePageController {

	private String facility;
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	@RequestMapping(value = "/home.html")
	public String getMainPage(ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> authoritiesData = facilityAuthzDAO.getAuthorities(user, facility);
		
		model.put("authorities", authoritiesData.get());
		return "frag/home";
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
