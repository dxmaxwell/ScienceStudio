/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConvertException class.
 *     
 */
package ca.sciencestudio.data.support;

import java.lang.Exception;

/**
 * @author maxweld
 *
 */
public class ConverterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ConverterException() {
		super();
	}

	public ConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConverterException(String message) {
		super(message);
	}

	public ConverterException(Throwable cause) {
		super(cause);
	}
}
