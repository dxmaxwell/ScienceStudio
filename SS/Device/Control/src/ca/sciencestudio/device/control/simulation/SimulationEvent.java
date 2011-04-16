/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationEvent class.
 *     
 */
package ca.sciencestudio.device.control.simulation;

import java.util.Date;

import ca.sciencestudio.device.control.simulation.SimulationEventType;

/**
 * @author maxweld
 *
 */
public class SimulationEvent {

	private String deviceId;
	private SimulationEventType type;
	private Object value;
	private Date timestamp;
	
	public SimulationEvent(SimulationEventType type, String deviceId, Object value) {
		this(type, deviceId, value, new Date());
	}
	
	public SimulationEvent(SimulationEventType type, String deviceId, Object value, Date timestamp) {
		this.type = type;
		this.deviceId = deviceId;
		this.value = value;
		this.timestamp = timestamp;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public SimulationEventType getType() {
		return type;
	}
	public Object getValue() {
		return value;
	}
	public Date getTimestamp() {
		return timestamp;
	}
}
