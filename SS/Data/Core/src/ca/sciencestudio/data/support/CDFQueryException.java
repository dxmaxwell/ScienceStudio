/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFQueryException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class CDFQueryException extends Exception {

	private static final long serialVersionUID = 1L;

	public CDFQueryException() {
		super();
	}

	public CDFQueryException(String message) {
		super(message);
	}

	public CDFQueryException(Throwable cause) {
		super(cause);
	}

	public CDFQueryException(String message, Throwable cause) {
		super(message, cause);
	}
}
