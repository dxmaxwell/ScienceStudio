/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    ExpandingProcessFactory class.
 *     
 */
package ca.sciencestudio.util.process;

import java.io.IOException;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;

import ca.sciencestudio.util.process.SimpleProcessFactory;

/**
 * @author maxweld
 *
 */
public class ExpandingProcessFactory extends SimpleProcessFactory {

	private static final String[] COMMAND_VARIABLE_REGEX_FMTS =  { "(\\$%s\\$)", "|(%%%s%%)", "|(\\$\\{%s\\})", "|(\\$%s\\s+)" };
	
	@Override
	public Process start(Map<String,String> environment, boolean redirectErrorStream) throws IOException {
		ProcessBuilder processBuilder = createProcessBuilder(environment, redirectErrorStream);
		expandEnironmentVariables(processBuilder);
		return processBuilder.start();
	}
	
	protected void expandEnironmentVariables(ProcessBuilder processBuilder) {
		
		List<String> command = processBuilder.command();
		Map<String,String> environment = processBuilder.environment();
		
		List<String> expandCommand = new ArrayList<String>();
		for(String cmd : command) {
			expandCommand.add(expandEnvironmentVariables(cmd, environment));
		}
		
		processBuilder.command(expandCommand);
	}
	
	protected String expandEnvironmentVariables(String target, Map<String,String> environment) {
		
		if(target == null) {
			return null; 
		}
		
		if(environment == null) {
			return target;
		}
	
		String environVarRegex;
		String environVarValue;
		for(Map.Entry<String,String> entry : environment.entrySet()) {
			environVarRegex = getEnvironmentVariableRegex(entry.getKey());
			environVarValue = Matcher.quoteReplacement(entry.getValue());
			target = target.replaceAll(environVarRegex, environVarValue);				
		}
			
		return target;
	}

	protected String getEnvironmentVariableRegex(String environmentVariable) {
		StringBuilder regex = new StringBuilder();
		for(String envVarRegexFmt : COMMAND_VARIABLE_REGEX_FMTS) {
			regex.append(String.format(envVarRegexFmt, environmentVariable));
		}
		return regex.toString();
	}	
}
