/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		ProcessDevice class.
 */
package ca.sciencestudio.device.control.component.impl;

import java.lang.Thread;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import ca.sciencestudio.device.control.component.DeviceComponent;

/**
 * @author maxweld
 *
 */
public class ProcessDevice extends DeviceComponent {

	public enum ProcessInputType { STDOUT, STDERR }
	
	private static final String DEFAULT_EXECUTABLE = "bash";
	private static final String[] DEFAULT_ARGUMENTS = {};
	
	protected Process process = null;
	protected ProcessBuilder processBuilder;
	
	public ProcessDevice(String id) {
		super(id);
		List<String> command = new ArrayList<String>();
		command.add(DEFAULT_EXECUTABLE); 
		command.addAll(Arrays.asList(DEFAULT_ARGUMENTS));
		processBuilder = new ProcessBuilder(command);
	}
	
	public void startProcess() {
		if(isProcessRunning()) {
			log.warn("Process already started.");
		}
		else {
			try {
				process = processBuilder.start();
				new ProcessInputThread(ProcessInputType.STDOUT, System.out);
				new ProcessInputThread(ProcessInputType.STDERR, System.err);
			}
			catch(IOException e) {
				log.warn("Process could not be started.");
			}
		}
	}
	
	public void stopProcess() {
		if(isProcessRunning()) {
			process.destroy();
			process = null;
		}
	}
	
	public Process getProcess() {
		return process;
	}
	
	public boolean isProcessRunning() {			
		try {
			process.exitValue();
			return false;
		}
		catch(NullPointerException e) {
			return false;
		}
		catch(IllegalThreadStateException e) {
			return true;
		}
	}
	
	public String getExecutable() {
		List<String> command = processBuilder.command();
		return command.get(0);
	}
	
	public void setExecutable(String executable) {
		List<String> command = processBuilder.command();
		command.add(0, executable);
	}
	
	public List<String> getArguments() {
		List<String> command = processBuilder.command();
		List<String> arguments = new ArrayList<String>(command);
		arguments.remove(0);
		return arguments;
	}
	
	public void setArguments(List<String> arguments) {
		List<String> command = processBuilder.command();
		String executable = command.get(0);
		command = new ArrayList<String>();
		command.add(executable);
		command.addAll(arguments);
		processBuilder.command(command);
	}
	
	public String getDirectory() {
		File directory = processBuilder.directory();
		return directory.getAbsolutePath();
	}
	
	public void setDirectory(String directory) {
		File dir = new File(directory);
		if(dir.isDirectory()) {
			processBuilder.directory(dir);
		}
	}
	
	private class ProcessInputThread extends Thread {
		
		private PrintStream out;
		private ProcessInputType type;
		
		public ProcessInputThread(ProcessInputType type, OutputStream out) {
			this.type = type;
			setPrintStream(out);
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			if(process == null) { return; }
			
			String readLine = null;
			InputStream inputStream = null;
			
			switch(type) {
				default:
				case STDOUT: 
					inputStream = process.getInputStream();
					break;
				
				case STDERR: 
					inputStream = process.getErrorStream();
					break;
			}
			if(inputStream == null) { return; }
			
			InputStreamReader inputStreamReader = 
					new InputStreamReader(inputStream);
			
			BufferedReader bufferedReader = 
					new BufferedReader(inputStreamReader);
			
			while(bufferedReader != null) {
				try {
					readLine = bufferedReader.readLine();
					if(readLine == null) { break; }
					out.print(">>>");
					out.println(readLine);
				}
				catch(IOException e) { break; }
			}
		}
		
		protected void setPrintStream(OutputStream out) {
			if(out instanceof PrintStream) {
				this.out = (PrintStream) out;
			}
			else {
				this.out = new PrintStream(out);
			}
		}
	}
}
