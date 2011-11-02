/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    CasAuthcAuthenticationEntryPoint class.
 *    See org.springframework.security.cas.web.CasAuthenticationEntryPoint;
 *     
 */
package ca.sciencestudio.security.spring.web.authentication.cas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author maxweld
 *
 */
public class CasAuthcAuthenticationEntryPoint implements AuthenticationEntryPoint {

	public static final String QUERY_SEPARATOR = "?";
	public static final String PARAM_SEPARATOR = "&";
	
	private static final String REQUEST_PARAM_AUTHENTICATOR = "authc";
	
	private String loginUrl;
	
	private ServiceProperties serviceProperties;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {
		
		String encodedService = createServiceUrl(request, response);
	    String redirectUrl = createRedirectUrl(request, response, encodedService);
	    
	    preCommence(request, response);
	    
	    response.sendRedirect(redirectUrl);
	}

    protected String createServiceUrl(HttpServletRequest request, HttpServletResponse response) {
        return CommonUtils.constructServiceUrl(null, response, this.serviceProperties.getService(), null, this.serviceProperties.getArtifactParameter(), true);
    }

    protected String createRedirectUrl(HttpServletRequest request, HttpServletResponse response, String serviceUrl) { 
        StringBuffer redirectUrl = new StringBuffer(CommonUtils.constructRedirectUrl(this.loginUrl, this.serviceProperties.getServiceParameter(), serviceUrl, this.serviceProperties.isSendRenew(), false));
        
        String authenticator = request.getParameter(REQUEST_PARAM_AUTHENTICATOR);
        if((authenticator != null) && (authenticator.length() > 0)) {
        	if(redirectUrl.indexOf(QUERY_SEPARATOR) < 0) {
    			redirectUrl.append(QUERY_SEPARATOR);
    		} else {
    			redirectUrl.append(PARAM_SEPARATOR);
    		}
        	redirectUrl.append(REQUEST_PARAM_AUTHENTICATOR);
        	redirectUrl.append("=").append(authenticator);
        }
        
        return redirectUrl.toString();
    }

    protected void preCommence(HttpServletRequest request, HttpServletResponse response) {

    }
    
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public ServiceProperties getServiceProperties() {
		return serviceProperties;
	}
	public void setServiceProperties(ServiceProperties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}
}
