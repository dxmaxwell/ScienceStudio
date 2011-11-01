/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AuthorizationException class.
 *     
 */
package ca.sciencestudio.util.exceptions;

/**
 * @author maxweld
 *
 */
public class AuthorizationException extends ModelAccessException {

	private static final long serialVersionUID = 1L;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}
}
