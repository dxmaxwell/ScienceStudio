/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SecurityImpl interface.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Collection;

import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public interface SecurityStrategy {

	public String getUsername();
	
	public boolean isAuthenticated();
	
	public Person getPerson();
	
	public boolean hasAuthority(Object authority);
	
	public boolean hasAnyAuthority(Object... authority);
	public boolean hasAnyAuthority(Collection<?> authorities);
	
	public boolean hasAllAuthorities(String... authorities);
	public boolean hasAllAuthorities(Collection<?> authorities);
}
