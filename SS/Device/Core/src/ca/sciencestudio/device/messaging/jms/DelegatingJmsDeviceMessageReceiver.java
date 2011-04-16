/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DelegatingJmsDeviceMessageReceiver class.
 * 
 */
package ca.sciencestudio.device.messaging.jms;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageReceiver;

/**
 * @author maxweld
 *
 */
public class DelegatingJmsDeviceMessageReceiver extends AbstractJmsDeviceMessageReceiver {

	private DeviceMessageReceiver deviceMessageReceiver;
	
	@Override
	public void receive(DeviceMessage<?> deviceMessage) {
		deviceMessageReceiver.receive(deviceMessage);
	}

	public DeviceMessageReceiver getDeviceMessageReceiver() {
		return deviceMessageReceiver;
	}
	public void setDeviceMessageReceiver(DeviceMessageReceiver deviceMessageReceiver) {
		this.deviceMessageReceiver = deviceMessageReceiver;
	}
}
