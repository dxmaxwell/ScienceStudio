/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceEventSender interface.
 */
package ca.sciencestudio.device.control.messaging;

import ca.sciencestudio.device.control.event.DeviceEvent;

/**
 * @author maxweld
 *
 */
public interface DeviceEventSender {

	public void send(DeviceEvent deviceEvent);
}
