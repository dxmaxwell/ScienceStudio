/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFTypesException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class CDFTypesException extends TypesException {

	private static final long serialVersionUID = 1L;

	public CDFTypesException() {
		super();
	}

	public CDFTypesException(String message, Throwable cause) {
		super(message, cause);
	}

	public CDFTypesException(String message) {
		super(message);
	}

	public CDFTypesException(Throwable cause) {
		super(cause);
	}
}
