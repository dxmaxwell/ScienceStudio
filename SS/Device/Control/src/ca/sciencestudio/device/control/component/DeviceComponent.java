/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceComponent class.
 */
package ca.sciencestudio.device.control.component;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.DeviceType;

/**
 * @author chabotd
 */
public abstract class DeviceComponent extends Device {

/**************************************************************************/
	protected DeviceComponent(String id) {
		super(id);
		deviceType = DeviceType.Simple;
	}

	/**
	 * Client code should NEVER use this method !!
	 */
	public synchronized void setStatus(DeviceStatus status) {
		this.status = status;
	}
	
}
