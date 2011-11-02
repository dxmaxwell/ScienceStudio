/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CasAuthcRedirectLogoutSuccessHandler class.
 *     
 */
package ca.sciencestudio.security.spring.web.authentication.cas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import ca.sciencestudio.security.cas.client.validation.ScienceStudioDelegatingAssertion;
import ca.sciencestudio.security.spring.cas.userdetails.CasScienceStudioUserDetails;

/**
 * @author maxweld
 * 
 *
 */
public class CasAuthcRedirectLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final String QUERY_SEPARATOR = "?";
	
	private static final String PARAM_SEPARATOR = "&";
	
	private String redirectUrl = "/";
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	protected Log logger = LogFactory.getLog(getClass());	
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		 String targetUrl = determineTargetUrl(authentication);

		 if (response.isCommitted()) {
			 logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			 return;
		 }
		 
		 redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(Authentication authentication) {
		
		if(authentication == null) {
			return redirectUrl;
		}
		
		String authenticator = null;
		
		// First check for facility name in the Science Studio Assertion. //
		if(authentication instanceof CasAssertionAuthenticationToken) {
			Assertion assertion = ((CasAssertionAuthenticationToken)authentication).getAssertion();
			if(assertion instanceof ScienceStudioDelegatingAssertion) {			
				authenticator = ((ScienceStudioDelegatingAssertion)assertion).getAuthenticator();
			}
		}
	
		// Second check for facility name in Science Studio User Details. //
		if(authenticator == null) {
			Object principal = authentication.getPrincipal();
			if(principal instanceof CasScienceStudioUserDetails) {
				authenticator = ((CasScienceStudioUserDetails)principal).getAuthenticator();
			}	
		}
	
		if((authenticator == null) || (authenticator.length() == 0)) {
			return redirectUrl;
		}
				
		StringBuffer targetUrl = new StringBuffer(redirectUrl);
		
		if(targetUrl.indexOf(QUERY_SEPARATOR) < 0) {
			targetUrl.append(QUERY_SEPARATOR);
		} else {
			targetUrl.append(PARAM_SEPARATOR);
		}

		return targetUrl.append("authc=").append(authenticator).toString();
	}
	
	
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}	
}
