/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DelegatingActiveMQDeviceEventReceiver class.
 * 
 */
package ca.sciencestudio.device.control.messaging.activemq;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventReceiver;

/**
 * @author maxweld
 *
 */
public class DelegatingActiveMQDeviceEventReceiver extends AbstractActiveMQDeviceEventReceiver {

	private DeviceEventReceiver deviceEventReceiver;
	
	public DelegatingActiveMQDeviceEventReceiver(String subject, boolean topic, String url) {
		super(subject, topic, url);
	}
	
	@Override
	public void receive(DeviceEvent deviceEvent) {
		deviceEventReceiver.receive(deviceEvent);
	}

	public void setDeviceEventReceiver(DeviceEventReceiver deviceEventReceiver) {
		this.deviceEventReceiver = deviceEventReceiver;
	}
}
