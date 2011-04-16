/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleDeviceEventReceiverPublisher class.
 * 
 */
package ca.sciencestudio.device.control.event.impl;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventReceiver;
import ca.sciencestudio.device.control.event.impl.AbstractDeviceEventPublisher;

/**
 * @author maxweld
 *
 */
public class SimpleDeviceEventReceiverPublisher extends AbstractDeviceEventPublisher implements DeviceEventReceiver {

	public void receive(DeviceEvent deviceEvent) {
		publishEvent(deviceEvent);
	}
}
