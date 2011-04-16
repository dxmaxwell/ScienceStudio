/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractDeviceProxyEventPublisher class.
 * 
 */
package ca.sciencestudio.device.proxy.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ca.sciencestudio.device.proxy.event.DeviceProxyEvent;
import ca.sciencestudio.device.proxy.event.DeviceProxyEventListener;

/**
 * @author maxweld
 *
 */
public class AbstractDeviceProxyEventPublisher implements DeviceProxyEventPublisher {

	protected List<DeviceProxyEventListener> deviceProxyEventListeners =  new ArrayList<DeviceProxyEventListener>();
	protected boolean publishingEvents = true;
	protected long publishedEvents = 0L;
	
	protected void publishEvent(final DeviceProxyEvent deviceProxyEvent) {
		if(publishingEvents) {
			for(DeviceProxyEventListener listener : deviceProxyEventListeners) {
				listener.handleEvent(deviceProxyEvent);
			}
			publishedEvents++;
		}
	}
	
	@Override
	public boolean isPublishingEvents() {
		return publishingEvents;
	}
	
	@Override
	public void setPublishingEvents(boolean publishingEvents) {
		if(this.publishingEvents != publishingEvents) {
			this.publishingEvents = publishingEvents;
			if(publishingEvents) { publishedEvents = 0L; }
		}
	}
	
	@Override
	public void startPublishingEvents() {
		setPublishingEvents(true);
	}
	
	@Override
	public void stopPublishingEvents() {
		setPublishingEvents(false);
	}
	
	@Override
	public boolean hasPublishedEvents() {
		return (publishedEvents > 0L);
	}
	
	@Override
	public long getPublishedEvents() {
		return publishedEvents;
	}
	
	@Override
	public Collection<DeviceProxyEventListener> getEventListeners() {
		return Collections.unmodifiableCollection(deviceProxyEventListeners);
	}

	@Override
	public void addEventListener(DeviceProxyEventListener deviceEventListener) {
		if(!deviceProxyEventListeners.contains(deviceEventListener)) {
			deviceProxyEventListeners.add(deviceEventListener);
		}
	}
	
	@Override
	public void removeEventListener(DeviceProxyEventListener deviceEventListener) {
		deviceProxyEventListeners.remove(deviceEventListener);
	}
}
