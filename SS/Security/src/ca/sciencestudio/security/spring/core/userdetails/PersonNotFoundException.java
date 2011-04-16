/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonNotFoundException class.
 *     
 */
package ca.sciencestudio.security.spring.core.userdetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author maxweld
 *
 */
public class PersonNotFoundException extends UsernameNotFoundException {

	private static final long serialVersionUID = 1L;

	public PersonNotFoundException(String msg) {
		super(msg);
	}
	
	public PersonNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public PersonNotFoundException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
