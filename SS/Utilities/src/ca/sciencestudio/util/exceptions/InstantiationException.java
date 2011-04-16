/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstantiationException class.  This exception has been adapted from JSecurity 0.9.0beta2.
 *     
 */
package ca.sciencestudio.util.exceptions;

/**
 * @author maxweld
 *
 */
public class InstantiationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InstantiationException() {
		super();
	}

	public InstantiationException( String message ) {
		super( message );
	}

	public InstantiationException( Throwable cause ) {
		super( cause );
	}

	public InstantiationException( String message, Throwable cause ) {
		super( message, cause );
	}
}
