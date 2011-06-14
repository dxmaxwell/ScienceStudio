/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SecurityUtilConfigBean class.
 *     
 */
package ca.sciencestudio.security.util;

import ca.sciencestudio.model.dao.ProjectPersonDAO;

/**
 * @author maxweld
 * 
 *
 */
public class SecurityUtilConfigBean {

	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		SecurityUtil.setProjectPersonDAO(projectPersonDAO);
	}
	
	public void setSecurityStrategy(SecurityStrategy securityStrategy) {
		SecurityUtil.setSecurityStrategy(securityStrategy);
	}
}
