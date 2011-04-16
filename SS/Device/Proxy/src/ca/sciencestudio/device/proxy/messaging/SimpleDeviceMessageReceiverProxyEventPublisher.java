/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleDeviceMessageReceiverProxyEventPublisher implementation.
 *     
 */
package ca.sciencestudio.device.proxy.messaging;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageReceiver;
import ca.sciencestudio.device.proxy.event.AbstractDeviceProxyEventPublisher;
import ca.sciencestudio.device.proxy.event.DeviceProxyEvent;
import ca.sciencestudio.device.proxy.event.DeviceProxyEventListener;

/**
 * @author maxweld
 *
 */
public class SimpleDeviceMessageReceiverProxyEventPublisher extends AbstractDeviceProxyEventPublisher implements DeviceMessageReceiver {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public SimpleDeviceMessageReceiverProxyEventPublisher(Collection<DeviceProxyEventListener> deviceProxyEventListeners) {
		for(DeviceProxyEventListener listener : deviceProxyEventListeners) {
			addEventListener(listener);
		}	
	}
		
	public void receive(DeviceMessage<?> deviceMessage) {
		if(logger.isDebugEnabled()) {
			logger.debug("Device message received for device id: " + deviceMessage.getDeviceId());
		}
		DeviceProxyEvent deviceProxyEvent = new DeviceProxyEvent();
		deviceProxyEvent.setDeviceId(deviceMessage.getDeviceId());
		deviceProxyEvent.setTimestamp(deviceMessage.getTimestamp());
		deviceProxyEvent.setValues(deviceMessage.getValue());
		publishEvent(deviceProxyEvent);
	}
}
