/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AccountNotFoundException class.
 *     
 */
package ca.sciencestudio.security.spring.core.userdetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author maxweld
 *
 */
public class AccountNotFoundException extends UsernameNotFoundException {

	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String msg) {
		super(msg);
	}

	public AccountNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	public AccountNotFoundException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
