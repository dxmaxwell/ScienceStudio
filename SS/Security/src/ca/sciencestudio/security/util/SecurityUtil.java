/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SecurityUtil abstract class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public abstract class SecurityUtil {
	
	private static SecurityStrategy SECURITY_STRATEGY = new DenySecurityStrategy();
	
	protected static Log LOGGER = LogFactory.getLog(SecurityUtil.class);
		
	static {	
		try {
			SECURITY_STRATEGY = new SpringSecurityStrategy();
		}
		catch(Exception e) {
			LOGGER.warn("Cannot instantiate new SpringSecurityStategy", e);
		}
	}
	
	public static String getUsername() {
		return SECURITY_STRATEGY.getUsername();
	}

	public static boolean isAuthenticated() {
		return SECURITY_STRATEGY.isAuthenticated();
	}
	
	public static Person getPerson() {
		return SECURITY_STRATEGY.getPerson();
	}
	
	public static boolean hasAuthority(Object authority) {
		return SECURITY_STRATEGY.hasAuthority(authority);
	}
	
	public static boolean hasAnyAuthority(Object... authority) {
		return SECURITY_STRATEGY.hasAnyAuthority(authority);
	}
	
	public static boolean hasAnyAuthority(Collection<?> authorities) {
		return SECURITY_STRATEGY.hasAnyAuthority(authorities);
	}
	
	public static boolean hasAllAuthorities(String... authorities) {
		return SECURITY_STRATEGY.hasAllAuthorities(authorities);
	}
	
	public static boolean hasAllAuthorities(Collection<?> authorities) {
		return SECURITY_STRATEGY.hasAllAuthorities(authorities);
	}
}
