/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      AbstractShareController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.io.File;

/**
 * @author maxweld
 *
 */
public abstract class AbstractShareController extends AbstractLaboratoryAuthzController {
	
	private File shareDirectory;
	
	public File getShareDirectory() {
		return shareDirectory;
	}
	
	public void setShareDirectory(File shareDirectory) {
		this.shareDirectory = shareDirectory.getAbsoluteFile();
		if(!this.shareDirectory.isDirectory()) {
			throw new IllegalArgumentException("Share directory does not exist or is not a directory.");
		}
	}
}
