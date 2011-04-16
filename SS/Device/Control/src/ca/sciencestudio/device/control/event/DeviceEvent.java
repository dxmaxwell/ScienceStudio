/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceEvent class.
 */
package ca.sciencestudio.device.control.event;

import java.io.Serializable;
import java.util.Date;

import ca.sciencestudio.device.control.DeviceAlarm;
import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.event.DeviceEventType;

/** 
 * @author chabotd
 *
 */
public class DeviceEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String deviceId;
	private DeviceEventType type;
	private DeviceStatus status;
	private Serializable value;
	private DeviceAlarm alarm;
	private Date timestamp;
/**************************************************************************/	
	public DeviceEvent(DeviceEventType type, String deviceId,  
				Serializable value, DeviceStatus status, DeviceAlarm alarm) {
		this(type, deviceId, value, status, alarm, new Date());		
	}
	
	public DeviceEvent(DeviceEventType type, String deviceId,  
			Serializable value, DeviceStatus status, DeviceAlarm alarm, Date timestamp) {
		this.deviceId = deviceId;
		this.type = type;
		this.value = value;
		this.status = status;
		this.alarm = alarm;
		this.timestamp = timestamp;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public DeviceEventType getDeviceEventType() {
		return type;
	}
	
	/**
	 * @return an array of one or more values, or null if
	 * the <code>DeviceComponent</code> is in an ERROR state.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @return the status of the Device
	 * @see DeviceStatus
	 */
	public DeviceStatus getStatus() {
		return status;
	}
	
	public String getAlarmStatus() {
		return alarm.getStatus();
	}
	
	public String getAlarmSeverity() {
		return alarm.getSeverity();
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
}

