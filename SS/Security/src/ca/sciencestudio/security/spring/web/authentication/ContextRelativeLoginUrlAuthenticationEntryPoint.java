/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	 ContextRelativeLoginUrlAuthenticationEntryPoint class.
 *     
 */
package ca.sciencestudio.security.spring.web.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;

/**
 * @author maxweld
 *
 */
public class ContextRelativeLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	private boolean contextRelative;

	protected final Log logger = LogFactory.getLog(LoginUrlAuthenticationEntryPoint.class);

	@Override
	protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response, 
			AuthenticationException authException) {
		////////////////////////////////////////////////////////
		// Logic for this method copied verbatim from parent, //
		// except now redirectStrategy is configuration.      //
		////////////////////////////////////////////////////////
		
		 String loginForm = determineUrlToUseForThisRequest(request, response, authException);

        if (UrlUtils.isAbsoluteUrl(loginForm)) {
            return loginForm;
        }

        int serverPort = getPortResolver().getServerPort(request);
        String scheme = request.getScheme();

        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();

        urlBuilder.setScheme(scheme);
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(serverPort);
        
        if(contextRelative) {
        	urlBuilder.setContextPath("");
        } else {
        	urlBuilder.setContextPath(request.getContextPath());        	
        }
        
        urlBuilder.setPathInfo(loginForm);

        if (isForceHttps() && "http".equals(scheme)) {
            Integer httpsPort = getPortMapper().lookupHttpsPort(Integer.valueOf(serverPort));

            if (httpsPort != null) {
                // Overwrite scheme and port in the redirect URL
                urlBuilder.setScheme("https");
                urlBuilder.setPort(httpsPort.intValue());
            } else {
                logger.warn("Unable to redirect to HTTPS as no port mapping found for HTTP port " + serverPort);
            }
        }

        return urlBuilder.getUrl();
	}

	public boolean isContextRelative() {
		return contextRelative;
	}

	public void setContextRelative(boolean contextRelative) {
		this.contextRelative = contextRelative;
	}
}
