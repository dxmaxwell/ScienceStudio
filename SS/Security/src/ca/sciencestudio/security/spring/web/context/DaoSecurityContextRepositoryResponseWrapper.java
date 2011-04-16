/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DaoSecurityContextRepositoryResponseWrapper class.
 *     
 */
package ca.sciencestudio.security.spring.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * @author maxweld
 *
 */
public class DaoSecurityContextRepositoryResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {
	
	private HttpServletRequest request;
	private SecurityContextRepository securityContextRepository;
	
	public DaoSecurityContextRepositoryResponseWrapper(HttpServletRequest request, HttpServletResponse response, 
																SecurityContextRepository securityContextRepository) {
		this(request, response, securityContextRepository, false);
	}
	
	public DaoSecurityContextRepositoryResponseWrapper(HttpServletRequest request, HttpServletResponse response, 
									SecurityContextRepository securityContextRepository, boolean disableUrlRewriting) {
		super(response, disableUrlRewriting);
		this.securityContextRepository = securityContextRepository;
		this.request = request;	
	}
	
	@Override
	protected void saveContext(SecurityContext securityContext) {
		securityContextRepository.saveContext(securityContext, request, this);
	}

	public HttpServletRequest getRequest() {
		return request;
	}
}
