/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DelegatingJmsDeviceEventReceiver class.
 * 
 */
package ca.sciencestudio.device.control.messaging.jms;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventReceiver;
import ca.sciencestudio.device.control.messaging.jms.AbstractJmsDeviceEventReceiver;

/**
 * @author maxweld
 *
 */
public class DelegatingJmsDeviceEventReceiver extends AbstractJmsDeviceEventReceiver {

	private DeviceEventReceiver deviceEventReceiver;
	
	@Override
	public void receive(DeviceEvent deviceEvent) {
		deviceEventReceiver.receive(deviceEvent);
	}

	public void setDeviceEventReceiver(DeviceEventReceiver deviceEventReceiver) {
		this.deviceEventReceiver = deviceEventReceiver;
	}
}
