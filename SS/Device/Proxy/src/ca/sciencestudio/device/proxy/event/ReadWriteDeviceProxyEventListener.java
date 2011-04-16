/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		ReadWriteDeviceProxyEventListener implementation.
 *     
 */
package ca.sciencestudio.device.proxy.event;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageType;

/**
 * @author maxweld
 *
 */
public class ReadWriteDeviceProxyEventListener extends ReadOnlyDeviceProxyEventListener {
	
	@Override
	public Serializable put(String key, Serializable value) {
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		values.put(key, value);
		putAll(values);
		return null;
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends Serializable> values) {
		DeviceMessage<HashMap<String,Serializable>> deviceMessage =
						new DeviceMessage<HashMap<String,Serializable>>();
		deviceMessage.setDeviceId(getDeviceId());
		deviceMessage.setTimestamp(new Date());
		deviceMessage.setValue(new HashMap<String,Serializable>(values));
		deviceMessage.setDeviceMessageType(DeviceMessageType.VALUE_UPDATE);
		deviceMessageSender.send(deviceMessage);
	}
}
