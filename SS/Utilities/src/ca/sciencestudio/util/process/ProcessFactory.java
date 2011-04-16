/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    ProcessFactory interface.
 *     
 */
package ca.sciencestudio.util.process;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.List;

/**
 * @author maxweld
 *
 */
public interface ProcessFactory {

	public Process start() throws IOException;
	public Process start(boolean redirectErrorStream) throws IOException;
	public Process start(Map<String,String> environment) throws IOException;
	public Process start(Map<String,String> environment, boolean redirectErrorStream) throws IOException;
	
	public List<String> getCommand();
	public void setCommand(String command);
	public void setCommand(List<String> command);
	
	public File getDirectory();
	public void setDirectory(File directory);

	public boolean isClearSystemEnvironment();
	public void setClearSystemEnvironment(boolean clearSystemEnvironment);
	
	public Map<String, String> getEnvironment();
	public void setEnvironment(Map<String, String> environment);
}
