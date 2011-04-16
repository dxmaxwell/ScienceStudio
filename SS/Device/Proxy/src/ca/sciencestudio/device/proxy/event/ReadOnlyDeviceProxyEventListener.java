/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		ReadOnlyDeviceProxyEventListener class.
 *     
 */
package ca.sciencestudio.device.proxy.event;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageType;
import ca.sciencestudio.device.messaging.DeviceMessageSender;
import ca.sciencestudio.device.proxy.SimpleDeviceProxy;
import ca.sciencestudio.device.proxy.event.DeviceProxyEventListener;

/**
 * @author maxweld
 *
 */
public class ReadOnlyDeviceProxyEventListener extends SimpleDeviceProxy implements DeviceProxyEventListener {

	protected DeviceMessageSender deviceMessageSender;
	
	@Override
	public void clear() {
		super.clear();
		queryDevice();
	}
	
	public void queryDevice() {
		DeviceMessage<HashMap<String,Serializable>> deviceMessage =
						new DeviceMessage<HashMap<String,Serializable>>();
		deviceMessage.setDeviceId(getDeviceId());
		deviceMessage.setTimestamp(getTimestamp());
		deviceMessage.setValue(new HashMap<String,Serializable>());
		deviceMessage.setDeviceMessageType(DeviceMessageType.VALUE_QUERY);
		deviceMessageSender.send(deviceMessage);
	}
	
	public void handleEvent(DeviceProxyEvent deviceProxyEvent) {
		if(getDeviceId().equals(deviceProxyEvent.getDeviceId())) {	
			timestamp = deviceProxyEvent.getTimestamp();
			if(timestamp == null) {
				timestamp = new Date();
			}
			stateMap.putAll(deviceProxyEvent.getValues());
		}
	}

	public DeviceMessageSender getDeviceMessageSender() {
		return deviceMessageSender;
	}
	public void setDeviceMessageSender(DeviceMessageSender deviceMessageSender) {
		this.deviceMessageSender = deviceMessageSender;
	}
}
