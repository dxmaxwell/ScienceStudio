/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleDeviceMessageReceiverEventPublisher class.
 */
package ca.sciencestudio.device.control.messaging;

import java.io.Serializable;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageReceiver;
import ca.sciencestudio.device.control.event.impl.AbstractDeviceEventPublisher;

/**
 * @author maxweld
 *
 */
public class SimpleDeviceMessageReceiverEventPublisher extends AbstractDeviceEventPublisher implements DeviceMessageReceiver {
	
	public SimpleDeviceMessageReceiverEventPublisher() {
		startPublishingEvents();
	}
	
	public void receive(DeviceMessage<?> deviceMessage) {
		
		DeviceEvent deviceEvent = null;
		
		switch (deviceMessage.getDeviceMessageType()) {
			case VALUE_UPDATE:
				deviceEvent = new DeviceEvent(DeviceEventType.SET_VALUE, deviceMessage.getDeviceId(), (Serializable)deviceMessage.getValue(), null, null);
				break;
				
			case VALUE_QUERY:
				deviceEvent = new DeviceEvent(DeviceEventType.GET_VALUE, deviceMessage.getDeviceId(), (Serializable)deviceMessage.getValue(), null, null);
				break;
				
			case VALUE_CHANGE:
				break;
		}
		
		if(deviceEvent != null) {
			publishEvent(deviceEvent);
		}
	}
}
