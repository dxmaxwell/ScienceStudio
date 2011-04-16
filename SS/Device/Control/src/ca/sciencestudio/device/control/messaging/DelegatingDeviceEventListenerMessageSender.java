/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DelegatingDeviceEventListenerMessageSender class.
 * 
 */
package ca.sciencestudio.device.control.messaging;

import java.util.List;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageSender;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;
import ca.sciencestudio.device.control.messaging.AbstractDeviceEventListenerMessageSender;

/**
 * @author maxweld
 *
 */
public class DelegatingDeviceEventListenerMessageSender extends AbstractDeviceEventListenerMessageSender {

	private DeviceMessageSender deviceMessageSender;
	
	public DelegatingDeviceEventListenerMessageSender(List<DeviceEventPublisher> eventPublishers) {
		super(eventPublishers);
	}
	
	@Override
	public void send(DeviceMessage<?> deviceMessage) {
		deviceMessageSender.send(deviceMessage);
	}

	public void setDeviceMessageSender(DeviceMessageSender deviceMessageSender) {
		this.deviceMessageSender = deviceMessageSender;
	}
}
