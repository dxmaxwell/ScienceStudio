/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DeviceMessage class
 *     
 */
package ca.sciencestudio.device.messaging;

import java.io.Serializable;
import java.util.Map;

import ca.sciencestudio.device.messaging.DeviceMessageType;
import ca.sciencestudio.util.messaging.GenericMessage;

/**
 * @author maxweld
 *
 */
public class DeviceMessage<T extends Serializable & Map<String,Serializable>> extends GenericMessage<T> {

	private static final long serialVersionUID = 1L;
	
	private String deviceId;
	private DeviceMessageType deviceMessageType;

	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public DeviceMessageType getDeviceMessageType() {
		return deviceMessageType;
	}
	public void setDeviceMessageType(DeviceMessageType deviceMessageType) {
		this.deviceMessageType = deviceMessageType;
	}
}
