/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     ExceptionAdapter class.
 */
package ca.sciencestudio.device.control.support;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/** 
 *  @author chabotd
 * 
 * Shamelessly pilfered from Bruce Eckel's blog:
 * 	http://www.mindview.net/Etc/Discussions/CheckedExceptions
 * 
 */
public class ExceptionAdapter extends RuntimeException {
	
	private static final long serialVersionUID = 1935445827067851755L;
	private final String stackTrace;
	public Exception originalException;
	
	public ExceptionAdapter(Exception e) {
		super(e.toString());
		originalException = e;
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		stackTrace = sw.toString();
	}
	
	public ExceptionAdapter(String str) {
		super(str);
		stackTrace = str;
	}
	
	public void printStackTrace() { 
		printStackTrace(System.err);
	}
	
	public void printStackTrace(PrintStream s) { 
		synchronized(s) {
			s.print(getClass().getName() + ": ");
			s.print(stackTrace);
		}
	}
	
	public void printStackTrace(PrintWriter s) { 
		synchronized(s) {
			s.print(getClass().getName() + ": ");
			s.print(stackTrace);
		}
	}
	
	public void rethrow() throws Exception { 
		throw originalException;
	}
}
