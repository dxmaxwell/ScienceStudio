/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    Strategy interface.
 *     
 */
package ca.sciencestudio.security;

/**
 * @author maxweld
 *
 */
public interface Strategy {

	public String getUsername();
	
	public String getPersonGid();
	
	public String getAuthenticator();
	
	public boolean isAuthenticated();
}
