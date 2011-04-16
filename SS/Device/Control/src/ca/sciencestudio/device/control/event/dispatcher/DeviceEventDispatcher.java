/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    DeviceEventDispatcher abstract class.
 */
package ca.sciencestudio.device.control.event.dispatcher;

import java.util.List;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.dispatcher.DeviceEventDispatcherImpl;
import ca.sciencestudio.device.control.event.dispatcher.impl.DefaultDeviceEventDispatcherImpl;
import ca.sciencestudio.device.control.event.dispatcher.impl.StatisticDeviceEventDispatcherImpl;

/**
 * @author maxweld
 *
 */
public abstract class DeviceEventDispatcher {

	private static DeviceEventDispatcherImpl dispatcherImpl = null; 
	
	public static DeviceEventDispatcherImpl getDispatcherImpl() {
		if(dispatcherImpl == null) {
			dispatcherImpl = getDispatcherImplInstance();
		}
		return dispatcherImpl;
	}
	
	public static void setDispatcherImpl(DeviceEventDispatcherImpl dispatcherImpl) {
		if(DeviceEventDispatcher.dispatcherImpl == null) { 
			DeviceEventDispatcher.dispatcherImpl = dispatcherImpl;
		}
	}
	
	public static boolean isDispatcherImpl() {
		return (dispatcherImpl != null);
	}
	
	public static void dispatchEvent(List<DeviceEventListener> deviceEventListeners, DeviceEvent deviceEvent) {
		getDispatcherImpl().dispatchEvent(deviceEventListeners, deviceEvent);
	}
	
	public static boolean isDispatchingEvents() {
		return getDispatcherImpl().isDispatchingEvents();
	}
	
	public static void startDispatchingEvents() {
		getDispatcherImpl().startDispatchingEvents();
	}
		
	public static void stopDispatchingEvents() {
		getDispatcherImpl().startDispatchingEvents();
	}
	
	public static boolean hasDispatchedEvents() {
		return getDispatcherImpl().hasDispatchedEvents();
	}
	
	public static long getDispatchedEvents() {
		return getDispatcherImpl().getDispatchedEvents();
	}
	
	protected static DeviceEventDispatcherImpl getDispatcherImplInstance() {
		String statistics = System.getProperty("bcm.dispatcher.statistics", "false");
		if(Boolean.parseBoolean(statistics)) {
			return new StatisticDeviceEventDispatcherImpl();
		}
		return new DefaultDeviceEventDispatcherImpl();
	}
}
