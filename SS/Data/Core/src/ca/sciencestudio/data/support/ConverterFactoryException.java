/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConvertRequestException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class ConverterFactoryException extends ConverterException {

	private static final long serialVersionUID = 1L;

	public ConverterFactoryException() {
		super();
	}

	public ConverterFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConverterFactoryException(String message) {
		super(message);
	}

	public ConverterFactoryException(Throwable cause) {
		super(cause);
	}
}
