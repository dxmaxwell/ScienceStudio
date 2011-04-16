/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      AbstractShareController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.io.File;

import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;

/**
 * @author maxweld
 *
 */
public abstract class AbstractShareController {
	
	private File shareDirectory;
	protected NanofabSessionStateMap nanofabSessionStateMap;
	
	public File getShareDirectory() {
		return shareDirectory;
	}
	
	public void setShareDirectory(File shareDirectory) {
		this.shareDirectory = shareDirectory.getAbsoluteFile();
		if(!this.shareDirectory.isDirectory()) {
			throw new IllegalArgumentException("Share directory does not exist or is not a directory.");
		}
	}
	
	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
}
