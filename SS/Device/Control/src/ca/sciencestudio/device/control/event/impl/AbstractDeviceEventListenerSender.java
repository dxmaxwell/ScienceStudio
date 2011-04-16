/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractDeviceEventListenerSender class.
 * 
 */
package ca.sciencestudio.device.control.event.impl;

import java.util.List;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;

/**
 * @author maxweld
 *
 */
public abstract class AbstractDeviceEventListenerSender implements DeviceEventListener {

	public AbstractDeviceEventListenerSender(List<DeviceEventPublisher> eventPublishers) {
		for(DeviceEventPublisher eventPublisher : eventPublishers) {
			eventPublisher.addEventListener(this);
		}
	}

	public void handleEvent(DeviceEvent deviceEvent) {
		send(deviceEvent);
	}
	
	public abstract void send(DeviceEvent deviceEvent);
}
