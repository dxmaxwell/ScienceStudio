/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SimpleProcessFactory class.
 *     
 */
package ca.sciencestudio.util.process;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author maxweld
 *
 */
public class SimpleProcessFactory implements ProcessFactory {

	private List<String> command = new ArrayList<String>();
	private File directory = new File(System.getProperty("user.dir", "."));
	
	private boolean clearSystemEnvironment = true;
	private Map<String,String> environment = new HashMap<String,String>();
	
	protected Log log = LogFactory.getLog(getClass());
	
	public Process start() throws IOException {
		return start(false);
	}
	
	public Process start(boolean redirectErrorStream) throws IOException {
		Map<String,String> emptyEnvironment = Collections.emptyMap();
		return start(emptyEnvironment, redirectErrorStream);
	}
	
	public Process start(Map<String,String> environment) throws IOException {
		return start(environment, false);
	}
	
	public Process start(Map<String,String> environment, boolean redirectErrorStream) throws IOException {
		return createProcessBuilder(environment, redirectErrorStream).start();
	}
		
	protected ProcessBuilder createProcessBuilder(Map<String,String> environment, boolean redirectErrorStream) {
		ProcessBuilder processBuilder = new ProcessBuilder(getCommand());
		processBuilder.redirectErrorStream(redirectErrorStream);
		processBuilder.directory(getDirectory());
		if(isClearSystemEnvironment()) {
			processBuilder.environment().clear();
		}
		processBuilder.environment().putAll(getEnvironment());
		processBuilder.environment().putAll(environment);
		return processBuilder;
	}
	
	protected String toString(List<String> commands) {
		boolean first = true;
		StringBuffer buffer = new StringBuffer();
		for(String command : commands) {
			if(first) {
				first = false;
				buffer.append(command);
			} else {
				buffer.append(" \"");
				buffer.append(command);
				buffer.append("\"");
			}
		}
		return buffer.toString();
	}
	
	public List<String> getCommand() {
		return command;
	}
	public void setCommand(List<String> command) {
		this.command = command;
	}
	public void setCommand(String command) {
		List<String> commands = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(command);
		while(tokenizer.hasMoreTokens()) {
			commands.add(tokenizer.nextToken());
		}
		setCommand(commands);
	}
	
	public File getDirectory() {
		return directory;
	}
	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public boolean isClearSystemEnvironment() {
		return clearSystemEnvironment;
	}
	public void setClearSystemEnvironment(boolean clearSystemEnvironment) {
		this.clearSystemEnvironment = clearSystemEnvironment;
	}
	
	public Map<String, String> getEnvironment() {
		return environment;
	}
	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}
	
	@Override
	public String toString() {
		return toString(getCommand());
	}
}
