/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelAccessException class.
 *     
 */
package ca.sciencestudio.model.dao.support;

/**
 * @author maxweld
 *
 */
public class ModelAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ModelAccessException() {
		super();
	}

	public ModelAccessException(String message) {
		super(message);
	}

	public ModelAccessException(Throwable cause) {
		super(cause);
	}

	public ModelAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
