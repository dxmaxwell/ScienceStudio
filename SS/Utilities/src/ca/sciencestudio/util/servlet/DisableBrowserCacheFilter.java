/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      DisableBrowserCacheFilter class.
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author maxweld
 *
 */
public class DisableBrowserCacheFilter implements Filter {

	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void destroy() {
		// nothing to do //
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(response instanceof HttpServletResponse) {
			disableCache((HttpServletResponse)response);
			logger.debug("Set HTTP servlet response headers to disable browser cache.");
		}
		else {
			logger.warn("Unable to set HTTP servlet response headers to disable browser cache.");
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// nothing to do //
	}

	protected void disableCache(HttpServletResponse response) {
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 1L);
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
	}
}
