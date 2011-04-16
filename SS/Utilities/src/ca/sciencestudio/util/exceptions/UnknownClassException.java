/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UnknownClassException class.  This exception has been adapted from JSecurity 0.9.0beta2.
 *     
 */
package ca.sciencestudio.util.exceptions;

/**
 * @author maxweld
 *
 */
public class UnknownClassException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UnknownClassException() {
		super();
	}

	public UnknownClassException(String message) {
		super(message);
	}

	public UnknownClassException(Throwable cause) {
		super(cause);
	}

	public UnknownClassException(String message, Throwable cause) {
		super(message, cause);
	}
}
