/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    CasLogoutController class.
 *     
 */
package ca.sciencestudio.login.service.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class CasLogoutController {
	
	public static final String CAS_LOGOUT_VIEW = "caslogout";
	
	public static final String MODEL_PARAM_LOGIN_URL = "loginUrl";
	public static final String MODEL_PARAM_COOKIE_NAME = "cookieName";
	public static final String MODEL_PARAM_COOKIE_PATH = "cookiePath";
	public static final String MODEL_PARAM_AUTHENTICATOR = "authenticator";
	public static final String MODEL_PARAM_FACILITY_LIST = "facilityList";
	
	
	public static final String DEFAULT_LOGIN_SESSION_COOKIE_NAME = "JSCIENCEID";
	public static final String DEFAULT_LOGIN_SESSION_COOKIE_PATH = "/";
		
	private String cookieName = DEFAULT_LOGIN_SESSION_COOKIE_NAME;
	private String cookiePath = DEFAULT_LOGIN_SESSION_COOKIE_PATH;

	private String loginUrl;
	private String processUrl;
		
	private FacilityAuthzDAO facilityAuthzDAO;
		
	@RequestMapping(value = "/logout/success")
	public String logoutSuccess(@RequestParam(required = false) String authc, HttpServletRequest request, ModelMap model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		if(!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:" + processUrl;
		}
		
		Facility facility = null;
		List<Facility> facilityList = facilityAuthzDAO.getAll().get();
		for(Facility f : facilityList) {
			if(f.getName().equalsIgnoreCase(authc)) {
				facility = f;
				break;
			}
		}
		
		String authenticator = null;
		if(facility != null) {
			authenticator = facility.getName();
			facilityList = Collections.singletonList(facility);
		}
		
		model.put(MODEL_PARAM_LOGIN_URL, loginUrl);
		model.put(MODEL_PARAM_COOKIE_PATH, cookiePath);
		model.put(MODEL_PARAM_COOKIE_NAME, cookieName);
		model.put(MODEL_PARAM_AUTHENTICATOR, authenticator);
		model.put(MODEL_PARAM_FACILITY_LIST, facilityList);
		return CAS_LOGOUT_VIEW;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getProcessUrl() {
		return processUrl;
	}
	public void setProcessUrl(String processUrl) {
		this.processUrl = processUrl;
	}
	
	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}

	public String getCookieName() {
		return cookieName;
	}
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getCookiePath() {
		return cookiePath;
	}
	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}
}
