/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TypesException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class TypesException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TypesException() {
		super();
	}

	public TypesException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypesException(String message) {
		super(message);
	}

	public TypesException(Throwable cause) {
		super(cause);
	}
}
