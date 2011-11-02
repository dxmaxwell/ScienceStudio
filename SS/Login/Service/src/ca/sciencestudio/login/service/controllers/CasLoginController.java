/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	CasLoginController class.
 *     
 */
package ca.sciencestudio.login.service.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class CasLoginController {

	public static final String CAS_LOGIN_VIEW = "caslogin";
	
	public static final String MODEL_PARAM_SERVICE = "service";
	public static final String MODEL_PARAM_FACILITY_LIST = "facilityList";
	
	public static final String REQUEST_PARAM_SERVICE = MODEL_PARAM_SERVICE;
	
	private String defaultService;
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(required = false) String authc, @RequestParam(required = false) String service, ModelMap model) {
		
		if((service == null) || (service.length() == 0)) {
			service = defaultService;
		}
		
		List<Facility> facilityList = facilityAuthzDAO.getAll().get();
		for(Facility facility : facilityList) {
			if(facility.getName().equalsIgnoreCase(authc)) {
				return constructRedirectUrl(facility, service);
			}
		}
		
		model.put(MODEL_PARAM_SERVICE, service);
		model.put(MODEL_PARAM_FACILITY_LIST, facilityList);
		return CAS_LOGIN_VIEW;
	}
	
	protected String constructRedirectUrl(Facility facility, String service) {
		StringBuffer redirectUrl = new StringBuffer("redirect:");
		redirectUrl.append(facility.getAuthcUrl()).append("/login");
		try {
			String encoded = URLEncoder.encode(service, "UTF-8");
			redirectUrl.append("?").append(REQUEST_PARAM_SERVICE);
			redirectUrl.append("=").append(encoded);
		}
		catch(UnsupportedEncodingException e) {
			// continue //
		}	
		return redirectUrl.toString();
	}
	
	public String getDefaultService() {
		return defaultService;
	}
	public void setDefaultService(String defaultService) {
		this.defaultService = defaultService;
	}
	
	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}
}
