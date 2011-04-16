/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    ProcessUtils class. Various utilities for handling system processes.
 *     
 */
package ca.sciencestudio.util.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread;
import java.lang.Process;

import org.apache.commons.io.IOUtils;

/**
 * @author maxweld
 *
 */
public class ProcessUtils {

	public static int waitFor(Process process, long millis) throws IllegalThreadStateException {
		return waitFor(process, millis, 0);
	}
	
	public static int waitFor(Process process, long millis, int nanos) throws IllegalThreadStateException {
		return waitFor(process, null, millis, nanos);
	}
	
	public static int waitFor(Process process, OutputStream stdoutStream, long millis) throws IllegalThreadStateException {
		return waitFor(process, stdoutStream, null, millis, 0);
	}
	
	public static int waitFor(Process process, OutputStream stdoutStream, long millis, int nanos) throws IllegalThreadStateException {
		return waitFor(process, stdoutStream, null, millis, nanos);
	}
	
	public static int waitFor(Process process, OutputStream stdoutStream, OutputStream stderrStream, long millis) throws IllegalThreadStateException {
		return waitFor(process, stdoutStream, stderrStream, millis, 0);
	}
	
	public static int waitFor(Process process, OutputStream stdoutStream, OutputStream stderrStream, long millis, int nanos) throws IllegalThreadStateException {
		if(process == null) { return 1; }
		
		Thread stdoutCopyThread = null;
		if(stdoutStream != null) {
			stdoutCopyThread = new StreamCopyThread(process.getInputStream(), stdoutStream);
		}
		
		Thread stderrCopyThread = null;
		if(stderrStream != null) {
			stderrCopyThread = new StreamCopyThread(process.getErrorStream(), stdoutStream);
		}
		
		Thread waitForThread = new WaitForThread(process);
		try {
			waitForThread.start();
			waitForThread.join(millis, nanos);
		}
		catch(InterruptedException e) {
			// Nothing To Do //
		}
		
		if((stdoutCopyThread != null) && stdoutCopyThread.isAlive()) {
			stdoutCopyThread.interrupt();
		}
		
		if((stderrCopyThread != null) && stderrCopyThread.isAlive()) {
			stderrCopyThread.interrupt();
		}
		
		if(waitForThread.isAlive()) {
			waitForThread.interrupt();
		}
		
		return process.exitValue();
	}
	
	private static class WaitForThread extends Thread {

		private Process process;
		
		public WaitForThread(Process process) {
			this.process = process;
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				process.waitFor();
			}
			catch(InterruptedException e) {
				// Nothing To Do //
			}
		}
	}
	
	private static class StreamCopyThread extends Thread {
		
		private InputStream inputStream;
		private OutputStream outputStream;
		
		public StreamCopyThread(InputStream inputStream, OutputStream outputStream) {
			this.inputStream = inputStream;
			this.outputStream = outputStream;
			setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			if((inputStream != null) && (outputStream != null)) {
				try {
					IOUtils.copy(inputStream, outputStream);
				}
				catch(IOException e) {
					// Nothing To Do //
				}
			}
		}
	}
}
