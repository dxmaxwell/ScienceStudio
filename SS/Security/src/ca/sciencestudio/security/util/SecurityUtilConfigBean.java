/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SecurityUtilConfigBean class.
 *     
 */
package ca.sciencestudio.security.util;

import ca.sciencestudio.security.Strategy;

/**
 * @author maxweld
 * 
 *
 */
public class SecurityUtilConfigBean {
	
	public void setSecurityStrategy(Strategy securityStrategy) {
		SecurityUtil.setSecurityStrategy(securityStrategy);
	}
}
