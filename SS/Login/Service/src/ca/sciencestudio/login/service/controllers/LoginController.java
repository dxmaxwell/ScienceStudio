/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	LoginController class.
 *     
 */
package ca.sciencestudio.login.service.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import ca.sciencestudio.util.net.QueryUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.login.service.backers.Credentials;
import ca.sciencestudio.login.service.validators.CredentialsValidator;
import ca.sciencestudio.security.spring.web.authentication.DomainUsernamePasswordAuthenticationFilter;

/**
 * @author maxweld
 *
 */
@Controller
public class LoginController {

	public static final String DEFAULT_LOGIN_FORM_VIEW = "login"; 
	
	private String loginFormView = DEFAULT_LOGIN_FORM_VIEW;
	
	private String casLoginUrl = null;
	private Map<String,String> domains = Collections.emptyMap();
	private CredentialsValidator credentialsValidator = null;
	
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String login(HttpServletRequest request, ModelMap model) {
		
		String error = request.getParameter("error");
		if(error == null) { 
			// No failed authentication attempt. //
			model.addAttribute(new Credentials());
			return loginFormView;
		}
		
		HttpSession session = request.getSession(false);
		if(session == null) {
			// Cannot access saved authentication credentials. //
			model.addAttribute(new Credentials());
			return loginFormView;
		}
		
		bindAndValidateCredentials(session, model);	
		return loginFormView;		
	}
	
	protected void bindAndValidateCredentials(HttpSession session, ModelMap model) {
		Map<String,Object> credentialsAttributes = new HashMap<String,Object>();
		credentialsAttributes.put("username", session.getAttribute(DomainUsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY));
		credentialsAttributes.put("domain", session.getAttribute(DomainUsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_DOMAIN_KEY));
		
		Credentials credentials = new Credentials();
		BindException errors = BindAndValidateUtils.bindAndValidate(credentials, credentialsAttributes, credentialsValidator);
		
		// Username and password are HTML escaped by UsernameDomainPasswordAuthenticationFilter, they need to be unescaped.
		credentials.setUsername(StringEscapeUtils.unescapeHtml(credentials.getUsername()));
		credentials.setDomain(StringEscapeUtils.unescapeHtml(credentials.getDomain()));
		
		if(!errors.hasErrors()) {
			Object authenticationException = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			if(authenticationException instanceof AuthenticationException) {
				errors.rejectValue("password", "login.password.error", authenticationException.toString());
			} else {
				errors.rejectValue("password", "login.password.unknown", "Unknown Authentication Error");
			}
		}
		
		model.addAllAttributes(errors.getModel());
	}
	
	public void getDomainsConfig(String domainsConfig) {
		QueryUtils.toSimpleQuery(getDomains());
	}
	public void setDomainsConfig(String domainsConfig) {
		setDomains(QueryUtils.toSimpleMap(domainsConfig));
	}
	
	@ModelAttribute("casLoginUrl")
	public String getCasLoginUrl() {
		return casLoginUrl;
	}
	public void setCasLoginUrl(String casLoginUrl) {
		this.casLoginUrl = casLoginUrl;
	}

	public String getLoginFormView() {
		return loginFormView;
	}
	public void setLoginFormView(String loginFormView) {
		this.loginFormView = loginFormView;
	}

	@ModelAttribute("domains")
	public Map<String, String> getDomains() {
		return domains;
	}
	public void setDomains(Map<String, String> domains) {
		this.domains = domains;
	}
	
	public CredentialsValidator getCredentialsValidator() {
		return credentialsValidator;
	}
	public void setCredentialsValidator(CredentialsValidator credentialsValidator) {
		this.credentialsValidator = credentialsValidator;
	}
}
