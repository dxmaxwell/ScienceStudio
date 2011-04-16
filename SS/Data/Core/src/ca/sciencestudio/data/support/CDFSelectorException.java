/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFSelectorException class.
 *     
 */
package ca.sciencestudio.data.support;

/**
 * @author maxweld
 *
 */
public class CDFSelectorException extends CDFQueryException {

	private static final long serialVersionUID = 1L;

	public CDFSelectorException() {
		super();
	}

	public CDFSelectorException(String message) {
		super(message);
	}

	public CDFSelectorException(Throwable cause) {
		super(cause);
	}

	public CDFSelectorException(String message, Throwable cause) {
		super(message, cause);
	}
}
