/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SpringSecurityStrategy class. SecurityUtils attempts to use this security strategy.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 *
 */
public class SpringSecurityStrategy implements SecurityStrategy {

	protected Log logger = LogFactory.getLog(getClass());
	
	public SpringSecurityStrategy() throws ClassNotFoundException {
		Class.forName("org.springframework.security.core.context.SecurityContextHolder");
	}
	
	@Override
	public String getUsername() {
		return getAuthentication().getName();
	}

	@Override
	public boolean isAuthenticated() {
		return getAuthentication().isAuthenticated();
	}
	
	@Override
	public Person getPerson() {
		Authentication auth = getAuthentication();
		if(auth == null) {
			return new UnknownPerson();
		}
		
		Object principal = auth.getPrincipal();
		if(!(principal instanceof ScienceStudioUserDetails)) {
			logger.error("Authentication object contains principle that is not ScienceStudioUserDetails");
			return new UnknownPerson();
		}
		
		return ((ScienceStudioUserDetails)principal).getPerson();
	}

	@Override
	public boolean hasAuthority(Object authority) {
		if(authority == null) {
			return false;
		}
		
		return getGrantedAuthorities().contains(authority.toString());
	}

	@Override
	public boolean hasAnyAuthority(Object... authorities) {
		return hasAnyAuthority(Arrays.asList(authorities));
	}

	@Override
	public boolean hasAnyAuthority(Collection<?> authorities) {
		if(authorities == null) {
			return false;
		}
		
		Set<String> grantedAuthorities = getGrantedAuthorities();
		
		for(Object authority : authorities) {
			if(grantedAuthorities.contains(authority.toString())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean hasAllAuthorities(String... authorities) {
		return hasAllAuthorities(Arrays.asList(authorities));
	}

	@Override
	public boolean hasAllAuthorities(Collection<?> authorities) {
		if(authorities == null) {
			return false;
		}
		
		Set<String> grantedAuthorities = getGrantedAuthorities();
		
		for(Object authority : authorities) {
			if(!grantedAuthorities.contains(authority.toString())) {
				return false;
			}
		}
		
		return true;
	}
	
	protected Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			logger.error("Security contexted returned NULL authentication!");
		}
		return auth;
	}
	
	protected Set<String> getGrantedAuthorities() {
		Authentication auth = getAuthentication();
		if(auth == null) {
			return Collections.emptySet();
		}
		
		Collection<GrantedAuthority> authorities = auth.getAuthorities();
		if(authorities == null) {
			return Collections.emptySet();
		}
		
		Set<String> grantedAuthorities = new HashSet<String>();
		for(GrantedAuthority authority : authorities) {
			grantedAuthorities.add(authority.getAuthority());
		}
		
		return grantedAuthorities;
	}
}
