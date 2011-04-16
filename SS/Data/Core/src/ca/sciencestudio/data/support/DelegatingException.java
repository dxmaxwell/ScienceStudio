/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingException class.
 *     
 */
package ca.sciencestudio.data.support;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author maxweld
 *
 */
public class DelegatingException extends Exception {

	private static final long serialVersionUID = 1L;

	private Collection<Throwable> exceptions = new LinkedHashSet<Throwable>();
	
	public DelegatingException() {
		super();
	}

	public DelegatingException(String message, Throwable cause) {
		super(message, cause);
	}

	public DelegatingException(String message) {
		super(message);
	}

	public DelegatingException(Throwable cause) {
		super(cause);
	}

	public void add(Throwable exception) {
		if(exception != null) {
			exceptions.add(exception);
		}
	}
	
	public void remove(Throwable exception) {
		exceptions.remove(exception);
	}
	
	@Override
	public String getMessage() {
		StringBuffer message = new StringBuffer();
		
		String msg = super.getMessage();
		if(msg != null) {
			message.append(msg);
		}
		
		for(Throwable exception : exceptions) {
			msg = exception.getMessage();
			if(msg != null) {
				message.append("\n");
				message.append(msg);
			}
		}
		return message.toString();
	}

	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}

	@Override
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	@Override
	public void printStackTrace(PrintStream s) {
		printStackTrace(new PrintWriter(s));
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		s.println("DelegatingException stack trace:");
		for(Throwable exception : exceptions) {
			exception.printStackTrace(s);
		}
	}

	public Collection<Throwable> getExceptions() {
		return exceptions;
	}
	
	public void setExceptions(Collection<Throwable> exceptions) {
		if(exceptions != null) {
			this.exceptions = exceptions;
		}
	}
}
