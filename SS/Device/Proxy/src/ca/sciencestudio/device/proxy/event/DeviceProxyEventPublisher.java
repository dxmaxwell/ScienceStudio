/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    DeviceProxyEventPublisher interface.
 */
package ca.sciencestudio.device.proxy.event;

import java.util.Collection;

import ca.sciencestudio.device.proxy.event.DeviceProxyEventListener;

/**
 * @author maxweld
 *
 */
public interface DeviceProxyEventPublisher {
	
	public boolean isPublishingEvents();
	public void setPublishingEvents(boolean publishingEvents);
	
	public void startPublishingEvents();
	public void stopPublishingEvents();
	
	public boolean hasPublishedEvents();
	public long getPublishedEvents();
	
	public Collection<DeviceProxyEventListener> getEventListeners();
	
	public void addEventListener(DeviceProxyEventListener deviceProxyEventListener);
	public void removeEventListener(DeviceProxyEventListener deviceProxyEventListener);
}
