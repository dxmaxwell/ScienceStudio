/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SpringSecurityStrategy class.
 *     
 */
package ca.sciencestudio.security.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.security.Strategy;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 *
 */
public class SpringSecurityStrategy implements Strategy {

	private static final String DEFAULT_USERNAME = "";
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public SpringSecurityStrategy() throws ClassNotFoundException {
		Class.forName("org.springframework.security.core.context.SecurityContextHolder");
	}
	
	@Override
	public String getUsername() {
		Authentication auth = getAuthentication();
		if(auth == null) {
			return DEFAULT_USERNAME;
		}
		return getAuthentication().getName();	
	}

	@Override
	public String getPersonGid() {
		ScienceStudioUserDetails userDetails = getUserDetails();
		if(userDetails == null) {
			return GID.DEFAULT_GID;
		}
		return userDetails.getPersonGid();
	}
	
	@Override
	public String getAuthenticator() {
		ScienceStudioUserDetails userDetails = getUserDetails();
		if(userDetails == null) {
			return GID.DEFAULT_GID;
		}
		return userDetails.getAuthenticator();
	}
	
	@Override
	public boolean isAuthenticated() {
		Authentication auth = getAuthentication();
		if(auth == null) {
			return false;
		}
		return auth.isAuthenticated();
	}
	
	protected ScienceStudioUserDetails getUserDetails() {
		Authentication auth = getAuthentication();
		if(auth == null) {
			return null;
		}
		Object principal = auth.getPrincipal();
		if(!(principal instanceof ScienceStudioUserDetails)) {
			logger.error("Authentication object contains principle that is not ScienceStudioUserDetails");
			return null;
		}
		return (ScienceStudioUserDetails)principal;
	}
	
	protected Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			logger.error("Security contexted returned NULL authentication!");
		}
		return auth;
	}
}
