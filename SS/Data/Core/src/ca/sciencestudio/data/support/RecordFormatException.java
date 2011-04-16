/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RecordFormatException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class RecordFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public RecordFormatException() {
		super();
	}

	public RecordFormatException(String message) {
		super(message);
	}

	public RecordFormatException(Throwable cause) {
		super(cause);
	}

	public RecordFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
