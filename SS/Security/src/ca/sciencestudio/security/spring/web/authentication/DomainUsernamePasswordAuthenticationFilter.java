/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	 DomainUsernamePasswordAuthenticationFilter class.
 *     
 */
package ca.sciencestudio.security.spring.web.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;

import ca.sciencestudio.security.spring.ldap.authentication.DomainUsernamePasswordAuthenticationToken;

/**
 * @author maxweld
 *
 */
public class DomainUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public static final String SPRING_SECURITY_LAST_DOMAIN_KEY = "SPRING_SECURITY_LAST_DOMAIN";
	
	public static final String SPRING_SECURITY_FORM_DOMAIN_KEY = "j_domain";
	
	private String domainParameter = SPRING_SECURITY_FORM_DOMAIN_KEY;
	
	private boolean postOnly = true;
	private boolean usernameWithDomain = true;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		////////////////////////////////////////////////////////////////////
		// Logic for this method is copied almost completely from parent. //
		////////////////////////////////////////////////////////////////////
		if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String domain = obtainDomain(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        
        if(domain == null) {
        	domain = "";
        }

        username = username.trim();
        domain = domain.trim();

        DomainUsernamePasswordAuthenticationToken authRequest = new DomainUsernamePasswordAuthenticationToken(domain, username, password);
        authRequest.setUseNameWithDomain(usernameWithDomain);
        
        // Place the last username attempted into HttpSession for views
        HttpSession session = request.getSession(false);

        if (session != null || getAllowSessionCreation()) {
            request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
            request.getSession().setAttribute(SPRING_SECURITY_LAST_DOMAIN_KEY, TextEscapeUtils.escapeEntities(domain));
        }

        // DomainUsernamePasswordAuthenticationToken already uses the "details" property.
        //setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	@Deprecated
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		super.setDetails(request, authRequest);
	}

	
	
	protected String obtainDomain(HttpServletRequest request) {
		return request.getParameter(domainParameter);
	}

	public String getDomainParameter() {
		return domainParameter;
	}

	public void setDomainParameter(String domainParameter) {
		this.domainParameter = domainParameter;
	}
	
	public boolean isPostOnly() {
		return postOnly;
	}
	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public boolean isUsernameWithDomain() {
		return usernameWithDomain;
	}
	public void setUsernameWithDomain(boolean usernameWithDomain) {
		this.usernameWithDomain = usernameWithDomain;
	}
}
