/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DelegatingDeviceEventListenerSender class.
 * 
 */
package ca.sciencestudio.device.control.event.impl;

import java.util.List;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;
import ca.sciencestudio.device.control.messaging.DeviceEventSender;

/**
 * @author maxweld
 *
 */
public class DelegatingDeviceEventListenerSender extends AbstractDeviceEventListenerSender {

	private DeviceEventSender deviceEventSender;
	
	public DelegatingDeviceEventListenerSender(List<DeviceEventPublisher> eventPublishers) {
		super(eventPublishers);
	}
	
	@Override
	public void send(DeviceEvent deviceEvent) {
		deviceEventSender.send(deviceEvent);
	}

	public void setDeviceEventSender(DeviceEventSender deviceEventSender) {
		this.deviceEventSender = deviceEventSender;
	}
}
