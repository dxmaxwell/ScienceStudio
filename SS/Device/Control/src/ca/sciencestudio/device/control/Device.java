/* Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    Device class.
 */
package ca.sciencestudio.device.control;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.device.control.DeviceAlarm;
import ca.sciencestudio.device.control.DeviceLocation;
import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.DeviceSubsystem;
import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.event.impl.AbstractDeviceEventPublisher;

/**
 * <p>Forms the base-class for a Composite structural pattern of parent (compound or node)
 *  and child (leaf) devices.</p>
 * Parent devices (<code>DeviceComposite</code>) are responsible for informing their children who the parent is, their Location, etc.
 * In turn, children (<code>DeviceComponent</code>) are responsible for setting their parent's <code>DeviceStatus</code>.
 * <p>For example, if any children are in a status other than <code>DeviceStatus.NORMAL</code>, so is
 * the parent.</p> 
 * 
 * @see <code>DeviceStatus</code>
 * @see <code>DeviceComposite</code>
 * @see <code>DeviceComponent</code>
 * 
 * @author chabotd
 */
public abstract class Device extends AbstractDeviceEventPublisher {

	protected List<DeviceEventListener> deviceEventListeners = new ArrayList<DeviceEventListener>();
	protected boolean publishingEvents = false;
	
	protected String id = null;
	protected String name = null;
	protected DeviceStatus status = DeviceStatus.ERROR;
	protected DeviceAlarm alarm = new DeviceAlarm();
	protected Device parent = null;
	protected DeviceLocation location = null;
	protected DeviceType deviceType = null;
	protected DeviceSubsystem subsystem = null;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public Device(String id) {
		this.id = id;
	}
	
	// Consider changing to abstract method.
	// Provides generic way to access device value(s).
	public Object getValue() { 
		return null; 
	};
	
	// Consider changing to abstract method.
	// Provides generic way to set device value(s).
	public void setValue(Object val) {};
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DeviceStatus getStatus() {
		return status;
	}
	
	public abstract void setStatus(DeviceStatus status);

	public void setDeviceAlarm(DeviceAlarm alarm) {
		this.alarm = alarm;
	}
	
	public DeviceAlarm getAlarm() {
		return alarm;
	}
	
	public String getAlarmStatus() {
		return alarm.getStatus();
	}
	
	public String getAlarmSeverity() {
		return alarm.getSeverity();
	}
	
	public Device getParent() {
		return parent;
	}
	
	public void setParent(Device parent) {
		this.parent = parent;
	}
	
	public DeviceLocation getLocation() {
		return location;
	}
	
	public DeviceSubsystem getSubsystem() {
		return subsystem;
	}

	public void setSubsystem(DeviceSubsystem subsystem) {
		this.subsystem = subsystem;
	}

	public void setLocation(DeviceLocation location) {
		this.location = location;
	}
	
	public DeviceType getDeviceType() {
		return deviceType;
	}
	
	public void publishValue() {
		publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, (Serializable)getValue(), status, alarm));
	}
}

