/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      RedirectContextRootFilter class.
 *     
 */
package ca.sciencestudio.util.servlet;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectContextRootFilter implements Filter {

	private static final String INIT_PARAM_REDIRECT_URL = "redirectUrl";
	private static final String INIT_PARAM_CONTEXT_RELATIVE = "contextRelative";
	
	private static final String PATH_SEPARATOR = "/";
	private static final String QUERY_SEPARATOR = "?";
	private static final String PARAM_SEPARATOR = "&";
	
	private URI redirectUrl;
	private boolean contextRelative;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		setRedirectUrl(filterConfig.getInitParameter(INIT_PARAM_REDIRECT_URL));
		setContextRelative(Boolean.valueOf(filterConfig.getInitParameter(INIT_PARAM_CONTEXT_RELATIVE)));
	}
	
	@Override
	public void destroy() {
		// nothing to do //
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if(!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String contextPath = httpRequest.getContextPath();
		String contextRoot = contextPath + PATH_SEPARATOR;
		if(!httpRequest.getRequestURI().equals(contextRoot)) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		
		if((redirectUrl == null) || (redirectUrl.isOpaque())) {
			chain.doFilter(request, response);
			return;
		}
		
		String url = redirectUrl.toString();		
		if(!redirectUrl.isAbsolute() && !contextRelative) {
			// This seems unusual to prepend context path if NOT context relative,
			// but this is the convention of Spring Security's DefaultRedirectStrategy.
			if(url.startsWith(PATH_SEPARATOR)) {
				url = contextPath + url;
			} else {
				url = contextRoot + url;
			}
		} 
			
		String queryString  = httpRequest.getQueryString();
		if((queryString != null) && (queryString.length() > 0)) {
			if(!url.contains(QUERY_SEPARATOR)) {
				url += QUERY_SEPARATOR + queryString;
			} else {
				url += PARAM_SEPARATOR + queryString;
			}
		}
		
		String encodedUrl = httpResponse.encodeRedirectURL(url);
		httpResponse.sendRedirect(encodedUrl);
		return;
	}

	public String getRedirectUrl() {
		if(redirectUrl == null) {
			return null;
		}
		return redirectUrl.toString();
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = URI.create(redirectUrl);
	}

	public boolean isContextRelative() {
		return contextRelative;
	}
	public void setContextRelative(boolean contextRelative) {
		this.contextRelative = contextRelative;
	}
}
