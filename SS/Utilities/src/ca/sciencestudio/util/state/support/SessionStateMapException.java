/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionStateMapException class.
 *     
 */
package ca.sciencestudio.util.state.support;

/**
 * @author maxweld
 *
 */
public class SessionStateMapException extends Exception {

	private static final long serialVersionUID = 1L;

	public SessionStateMapException() {
		super();
	}
	
	public SessionStateMapException(String message) {
		super(message);
	}

	public SessionStateMapException(Throwable cause) {
		super(cause);
	}
	
	public SessionStateMapException(String message, Throwable cause) {
		super(message, cause);
	}
}
