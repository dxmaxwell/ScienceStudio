/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    LogoutController class.
 *     
 */
package ca.sciencestudio.login.service.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.spring.web.context.DaoSecurityContextRepository;

/**
 * @author maxweld
 *
 */
@Controller
public class LogoutController {
	
	public static final String DEFAULT_LOGIN_FORM_URL = "/login";
	public static final String DEFAULT_LOGOUT_PROCESS_URL = "/logout";
	public static final String DEFAULT_LOGOUT_SUCCESS_VIEW = "logout";
	
	private String casLogoutUrl = null;
	private String loginFormUrl = DEFAULT_LOGOUT_PROCESS_URL;
	private String logoutProcessUrl = DEFAULT_LOGOUT_PROCESS_URL;
	private String logoutSuccessView = DEFAULT_LOGOUT_SUCCESS_VIEW;
	
	private String cookieName = DaoSecurityContextRepository.DEFAULT_LOGIN_SESSION_COOKIE_NAME;
	private String cookiePath = DaoSecurityContextRepository.DEFAULT_LOGIN_SESSION_COOKIE_PATH;
	
	@RequestMapping(value = "/logout/success.html", method = RequestMethod.GET)
	public String logoutSuccess(HttpServletRequest request, ModelMap model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		if(!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:" + logoutProcessUrl;
		}
		
		model.put("redirectUrl", getRedirectUrl(request));
		return logoutSuccessView;
	}

	
	protected String getRedirectUrl(HttpServletRequest request) {
		return request.getContextPath() + loginFormUrl;
	}
	
	@ModelAttribute("casLogoutUrl")
	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}
	public void setCasLogoutUrl(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}

	public String getLoginFormUrl() {
		return loginFormUrl;
	}
	public void setLoginFormUrl(String loginFormUrl) {
		this.loginFormUrl = loginFormUrl;
	}
	
	public String getLogoutProcessUrl() {
		return logoutProcessUrl;
	}
	public void setLogoutProcessUrl(String logoutProcessUrl) {
		this.logoutProcessUrl = logoutProcessUrl;
	}

	public String getLogoutSuccessView() {
		return logoutSuccessView;
	}
	public void setLogoutSuccessView(String logoutSuccessView) {
		this.logoutSuccessView = logoutSuccessView;
	}

	@ModelAttribute("cookieName")
	public String getCookieName() {
		return cookieName;
	}
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	@ModelAttribute("cookiePath")
	public String getCookiePath() {
		return cookiePath;
	}
	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}
}
