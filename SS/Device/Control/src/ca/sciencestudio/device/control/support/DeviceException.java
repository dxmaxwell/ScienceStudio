/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceException class.
 */
package ca.sciencestudio.device.control.support;

import ca.sciencestudio.device.control.support.ExceptionAdapter;

/**
 *  @author chabotd
 */
public class DeviceException extends ExceptionAdapter {

	private static final long serialVersionUID = -3053950473707320688L;

	public DeviceException(String str) {
		super(str);
	}
	
	public DeviceException(Exception e) {
		super(e);
	}
}
