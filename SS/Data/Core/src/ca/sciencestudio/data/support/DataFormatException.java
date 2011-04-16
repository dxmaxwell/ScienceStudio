/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DataFormatException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class DataFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataFormatException() {
		super();
	}

	public DataFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataFormatException(String message) {
		super(message);
	}

	public DataFormatException(Throwable cause) {
		super(cause);
	}
}
