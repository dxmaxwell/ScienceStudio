/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SecurityUtil abstract class.
 *     
 */
package ca.sciencestudio.security.util;

import ca.sciencestudio.security.Strategy;

/**
 * @author maxweld
 *
 */
public abstract class SecurityUtil {
		
	private static Strategy SECURITY_STRATEGY;
	
	public static Strategy getSecurityStrategy() {
		return SECURITY_STRATEGY;
	}
	public static void setSecurityStrategy(Strategy securityStrategy) {
		SECURITY_STRATEGY = securityStrategy;
	}
	
	public static String getUsername() {
		return SECURITY_STRATEGY.getUsername();
	}

	public static String getPersonGid() {
		return SECURITY_STRATEGY.getPersonGid();
	}
	
	public static String getAuthenticator() {
		return SECURITY_STRATEGY.getAuthenticator();
	}
	
	public static boolean isAuthenticated() {
		return SECURITY_STRATEGY.isAuthenticated();
	}	
}
