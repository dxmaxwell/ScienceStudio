/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceComposite abstract class.
 */
package ca.sciencestudio.device.control.composite;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.event.DeviceEventListener;

/**
 * The "parent" or "container" structure of the Composite (GoF) pattern
 * for <code>Devices</code>. Parameterized by type, T, where T is a subclass of
 * <code>Device</code>.
 * 
 * @author chabotd
 */
public abstract class DeviceComposite<T extends Device> extends Device {

	protected Map<String, T> components;
/**************************************************************************/
	public DeviceComposite(String id, Map<String,T> map) {
		super(id);
		components = map;
		Collection<T> coll = components.values();
		Iterator<T> iter = coll.iterator();
		while(iter.hasNext()) {
			Device dev = (Device) iter.next();
			dev.setParent(this);
			dev.setLocation(location);
			dev.setSubsystem(subsystem);
		}
	}
	
	public T getComponent(String name) {
		if(components.containsKey(name)) {
			return components.get(name);
		}
		return null;
	}
	
	public Map<String,T> getComponents() {
		return components;
	}
	
	/**
	 * FIXME !!!!!
	 * <p>This method is intended to be invoked ONLY by a child-device! Users of 
	 * this class should <b>NEVER</b> call this method...
	 * </p>
	 */
	public synchronized void setStatus(DeviceStatus status) {
		//defer event publication to our parent-device (if we have one)
		if(parent != null) {
			this.status = status;
			parent.setStatus(status);
			return;
		}
		//DeviceEvent de = new DeviceEvent(null, null);
		switch(this.status) {
			case ALARM: {
				switch(status) {
					case ALARM:
					case ERROR:
					case NORMAL:
					default:
				}
				
			}
			case ERROR: {
				
			}
			case NORMAL: {
				
			}
			default: {
				
			}
		}
		this.status = status;
	}
	
	/**
	 * Attach a <code>DeviceEventListener</code> to a specified <code>DeviceComponent</code>.
	 * 
	 * <p>
	 * @param componentId the ID of the target <code>DeviceComponent</code>.
	 * If a <code>null</code> <code>componentId</code> used, the container (parent)
	 * <code>DeviceComposite</code> becomes the target of the listener. The parent will publish
	 * <code>DeviceEventTypes</code> similarly to <code>DeviceComponent</code>, but with the exception
	 * that DeviceEvent.getValue() will return <code>null</code>.
	 * </p>
	 * @param listener The callback to invoke as a result of a <code>DeviceEvent</code> firing.
	 */
	public void addEventListener(String componentId, DeviceEventListener listener) {
		if(componentId != null) {
			Device device = components.get(componentId);
			if(device != null) { 
				device.addEventListener(listener);
			}
		}
		else {
			addEventListener(listener);
		}
	}
	
	/**
	 * 
	 * @param componentId
	 * @param listener
	 */
	public void removeEventListener(String componentId, DeviceEventListener listener) { 
		if(componentId != null) {
			Device device = components.get(componentId);
			if(device != null) {
				device.removeEventListener(listener);
			}
		}
		else {
			removeEventListener(listener);
		}
	} 
}
