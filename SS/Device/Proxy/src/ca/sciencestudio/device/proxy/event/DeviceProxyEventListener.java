/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DeviceProxyEventListener interface.
 *     
 */
package ca.sciencestudio.device.proxy.event;

import ca.sciencestudio.device.proxy.event.DeviceProxyEvent;

/**
 * @author maxweld
 *
 */
public interface DeviceProxyEventListener {

	public void queryDevice();
	public void handleEvent(DeviceProxyEvent deviceStateMapEvent);
}
