/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractCDFtoCDFMLConverter class.
 *     
 */
package ca.sciencestudio.data.converter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.util.process.ProcessFactory;
import ca.sciencestudio.util.process.ProcessUtils;
import ca.sciencestudio.data.support.ConverterException;

/**
 * @author maxweld
 *
 */
public abstract class AbstractCDFtoCDFMLConverter extends AbstractScanConverter {
	
	public static final long DEFAULT_PROCESS_TIMEOUT = 30000L; // 30 seconds //
	
	private static final String ENVIRONMENT_KEY_CDF_FILE = "CDF_FILE";
	private static final String ENVIRONMENT_KEY_CDFML_FILE = "CDFML_FILE";
	
	private File cdfDataFile = null;
	private File cdfmlDataFile = null;
	private ProcessFactory processFactory = null;
	private long processTimeout = DEFAULT_PROCESS_TIMEOUT;
	
	public AbstractCDFtoCDFMLConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}
	
	public ConverterMap convert() throws ConverterException {
		
		if(cdfDataFile == null) {
			throw new ConverterException("Required input CDF data file not specified.");
		}
		else if(!cdfDataFile.exists()) {
			throw new ConverterException("Required input CDF data file not found: " + cdfDataFile);
		}
		
		if(cdfmlDataFile == null) {
			throw new ConverterException("Require output CDFML data file not specified.");
		}
		
		if(cdfmlDataFile.exists()) {
			boolean cdfmlDelete = false;
			
			if(isForceUpdate()) {
				cdfmlDelete = true;
			}
			else if(cdfmlDataFile.lastModified() < cdfDataFile.lastModified()) {
				cdfmlDelete = true;
			}
			
			if(cdfmlDelete) {
				if(!cdfmlDataFile.delete()) {
					throw new ConverterException("Unable to remove existing CDFML file.");
				}
			}
			else {
				return getResponse();
			}
		}
		
		Map<String,String> environment = new HashMap<String,String>();
		environment.put(ENVIRONMENT_KEY_CDF_FILE, cdfDataFile.getAbsolutePath());
		environment.put(ENVIRONMENT_KEY_CDFML_FILE, cdfmlDataFile.getAbsolutePath());
		
		Process convertProcess;
		try {
			convertProcess = processFactory.start(environment, true);	
		}
		catch(IOException e) {
			throw new ConverterException("CDFtoCDFML process executable not found.", e);
		}
		
		OutputStream stdouterrStream = new ByteArrayOutputStream();
		try {
			if(ProcessUtils.waitFor(convertProcess, stdouterrStream, processTimeout) != 0) {
				 throw new ConverterException("CDFtoCDFML process exit status not zero.\n<<<stdouterr>>>\n"
						 						+ stdouterrStream + "\n<<<stdouterr>>>");
			 }
		}
		catch(IllegalThreadStateException e) {
			convertProcess.destroy();
			throw new ConverterException("CDFtoCDFML process did not exit before timeout.\n<<<stdouterr>>>\n"
												+ stdouterrStream + "\n<<<stdouterr>>>", e);
		}
		
		return getResponse();
	}

	public void setCdfDataFile(File cdfDataFile) {
		this.cdfDataFile = cdfDataFile;
	}
	
	public void setCdfmlDataFile(File cdfmlDataFile) {
		this.cdfmlDataFile = cdfmlDataFile;
	}

	public void setProcessFactory(ProcessFactory processFactory) {
		this.processFactory = processFactory;
	}

	public void setProcessTimeout(long processTimeout) {
		this.processTimeout = processTimeout;
	}
}
