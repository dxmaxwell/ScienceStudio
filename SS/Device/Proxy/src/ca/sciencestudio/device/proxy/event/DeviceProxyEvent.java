/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DeviceProxyEvent class.
 *     
 */
package ca.sciencestudio.device.proxy.event;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author maxweld
 *
 */
public class DeviceProxyEvent {

	String deviceId;
	Date timestamp;
	Map<String,Serializable> values;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Map<String, Serializable> getValues() {
		return values;
	}
	public void setValues(Map<String, Serializable> values) {
		this.values = values;
	}
}
