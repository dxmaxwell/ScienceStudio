/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanParameters class.
 *     
 */
package ca.sciencestudio.model.utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class ScanParameters extends Parameters {

	private static final String COMMENT_STRING = "Scan Parameters";
	
	private static final long serialVersionUID = 1L;
	
	private Scan scan;
	
	protected Log log = LogFactory.getLog(getClass());
	
	public ScanParameters() {
		this.scan = null;
	}
	
	public ScanParameters(Properties defaults) {
		super(defaults);
		this.scan = null;
	}
	
	public ScanParameters(Map<? extends String, ? extends String> defaults) {
		putAll(defaults);
		this.scan = null;
	}
	
	public ScanParameters(Scan scan) {
		this.scan = scan;
		load();
	}
	
	public ScanParameters(Scan scan, Properties defaults) {
		super(defaults);
		this.scan = scan;
		load();
	}
	
	public ScanParameters(Scan scan, Map<? extends String, ? extends String> defaults) {
		putAll(defaults);
		this.scan = scan;
		load();
	}
	
	public Scan getScan() {
		return scan;
	}
	
	public void load() {
		if(scan != null) {
			String scanParams = scan.getParameters();
			if(scanParams != null) {
				try {
					load(new ByteArrayInputStream(scanParams.getBytes()));
				}
				catch(IOException e) {
					log.warn("Exception while loading scan parameters from scan.", e);
				}
			}
		}
	}
	
	public void store() {
		if(scan != null) {
			try {
				OutputStream paramsOutputStream = new ByteArrayOutputStream();
				store(paramsOutputStream, getComment());
				scan.setParameters(paramsOutputStream.toString());
			}
			catch(IOException e) {
				log.warn("Exception while storing scan parameter in scan.", e);
			}
		}
	}
	
	protected String getComment() {
		return COMMENT_STRING;
	}
}
