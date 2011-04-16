/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleDeviceEventReceiverHandler class.
 * 
 */
package ca.sciencestudio.device.control.event.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.factory.DeviceFactory;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventReceiver;

/**
 * @author maxweld
 *
 */
public class SimpleDeviceEventReceiverHandler implements DeviceEventReceiver {

	protected Log logger = LogFactory.getLog(getClass());
	
	public void receive(DeviceEvent deviceEvent) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Device event recieved for device id: " + deviceEvent.getDeviceId() + ", and type: " + deviceEvent.getDeviceEventType());
		}
		
		String deviceId = deviceEvent.getDeviceId();
		
		if(deviceId == null) {
			logger.info("DeviceMessage has null deviceId. Will ignore message.");
			return;
		}
		
		Device device = DeviceFactory.getDevice(deviceId);
		if(device == null) {
			logger.info("Device can not be found with id, " + deviceId + ". Will ignore message.");
			return;
		}
		
		switch (deviceEvent.getDeviceEventType()) {
			case SET_VALUE:
				device.setValue(deviceEvent.getValue());
				break;
				
			case GET_VALUE:
				device.publishValue();
				break; 
		}
	}
}
