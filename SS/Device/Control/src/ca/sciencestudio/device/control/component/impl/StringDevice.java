/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *		StringDevice class.
 */
package ca.sciencestudio.device.control.component.impl;

import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.support.DeviceException;
import ca.sciencestudio.device.control.component.DeviceComponent;

/**
 * @author chabotd
 * 
 */
public class StringDevice extends DeviceComponent {

	private String string = null;
/**************************************************************************/
	public StringDevice(String id) {
		super(id);
		string = new String(id);
	}

	public void addEventListener(DeviceEventListener listener) {
		throw new DeviceException("Not supported");
	}

	public Object getValue() {
		return string;
	}

	protected void publishEvent(DeviceEvent event) {
		//not supported
	}

	public void removeEventListener(DeviceEventListener listener) {
		throw new DeviceException("Not supported");
	}

	public void setValue(Object val) {
		string = (String) val;
	}
	
	public String toString() {
		return string;
	}

	public void setStatus(DeviceStatus status) {
		this.status = status;
	}
}
