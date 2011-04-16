/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DelegatingActiveMQDeviceMessageReceiver class.
 * 
 */
package ca.sciencestudio.device.messaging.activemq;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageReceiver;
import ca.sciencestudio.device.messaging.activemq.AbstractActiveMQDeviceMessageReceiver;

/**
 * @author maxweld
 *
 */
public class DelegatingActiveMQDeviceMessageReceiver extends AbstractActiveMQDeviceMessageReceiver {

	private DeviceMessageReceiver deviceMessageReceiver;
	
	public DelegatingActiveMQDeviceMessageReceiver(String subject, boolean topic, String url) {
		super(subject, topic, url);
	}
	
	@Override
	public void receive(DeviceMessage<?> deviceMessage) {
		deviceMessageReceiver.receive(deviceMessage);
	}

	public void setDeviceMessageReceiver(DeviceMessageReceiver deviceMessageReceiver) {
		this.deviceMessageReceiver = deviceMessageReceiver;
	}
}
