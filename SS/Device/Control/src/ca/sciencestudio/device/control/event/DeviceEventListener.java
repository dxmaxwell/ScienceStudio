/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceEventListener interface.
 */
package ca.sciencestudio.device.control.event;

import java.util.EventListener;

/**
 * @author maxweld
 *
 */
public interface DeviceEventListener extends EventListener {
	
	public void handleEvent(DeviceEvent aEvent);
}
