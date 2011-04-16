/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceEventReceiver interface.
 */
package ca.sciencestudio.device.control.messaging;

import ca.sciencestudio.device.control.event.DeviceEvent;

/**
 * @author maxweld
 *
 */
public interface DeviceEventReceiver {

	public void receive(DeviceEvent deviceEvent);
}
