/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      RedirectContextRootFilter class.
 *     
 */
package ca.sciencestudio.util.servlet;

import java.io.IOException;

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
	
	private String redirectUrl;
	private boolean contextRelative;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		redirectUrl = filterConfig.getInitParameter(INIT_PARAM_REDIRECT_URL);
		contextRelative = Boolean.valueOf(filterConfig.getInitParameter(INIT_PARAM_CONTEXT_RELATIVE));
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
		
		if((redirectUrl == null) || (redirectUrl.length() == 0)) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}
		
		StringBuffer url = new StringBuffer();
		if(contextRelative) {
			if(redirectUrl.startsWith(PATH_SEPARATOR)) {
				url.append(contextPath);
			} else {
				url.append(contextRoot);
			}
		}
		
		url.append(redirectUrl);
		
		String queryString  = httpRequest.getQueryString();
		if((queryString != null) && (queryString.length() > 0)) {
			url.append(QUERY_SEPARATOR).append(queryString);
		}
		
		((HttpServletResponse)response).sendRedirect(url.toString());	
		return;
	}
}
