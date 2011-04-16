/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    DenySecurityStrategy class. Denies all security requests.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Collection;

import ca.sciencestudio.security.util.DummySecurityStrategy;

/**
 * @author maxweld
 *
 */
public class DenySecurityStrategy extends DummySecurityStrategy {

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public boolean hasAuthority(Object authority) {
		return false;
	}

	@Override
	public boolean hasAnyAuthority(Object... authority) {
		return false;
	}

	@Override
	public boolean hasAnyAuthority(Collection<?> authorities) {
		return false;
	}

	@Override
	public boolean hasAllAuthorities(String... authorities) {
		return false;
	}

	@Override
	public boolean hasAllAuthorities(Collection<?> authorities) {
		return false;
	}
}
