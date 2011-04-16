/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AllowSecurityStrategy class. Allows all security requests.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Collection;

/**
 * @author maxweld
 *
 */
public class AllowSecurityStrategy extends DummySecurityStrategy {

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public boolean hasAuthority(Object authority) {
		return true;
	}

	@Override
	public boolean hasAnyAuthority(Object... authority) {
		return true;
	}

	@Override
	public boolean hasAnyAuthority(Collection<?> authorities) {
		return true;
	}

	@Override
	public boolean hasAllAuthorities(String... authorities) {
		return true;
	}

	@Override
	public boolean hasAllAuthorities(Collection<?> authorities) {
		return true;
	}
}
