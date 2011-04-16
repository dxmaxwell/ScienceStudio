/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    DeviceEventDispatcherImpl interface.
 */
package ca.sciencestudio.device.control.event.dispatcher;

import java.util.List;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;

/**
 * @author maxweld
 *
 */
public interface DeviceEventDispatcherImpl {

	public void dispatchEvent(List<DeviceEventListener> deviceEventListeners, DeviceEvent deviceEvent);
	
	public boolean isDispatchingEvents();
	public void setDispatchingEvents(boolean dispatchingEvents);
	
	public void startDispatchingEvents();
	public void stopDispatchingEvents();
	
	public boolean hasDispatchedEvents();
	public long getDispatchedEvents();
}
