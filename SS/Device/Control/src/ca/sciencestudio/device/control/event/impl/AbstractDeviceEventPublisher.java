/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractDeviceEventPublisher class.
 * 
 */
package ca.sciencestudio.device.control.event.impl;

import java.util.List;
import java.util.ArrayList;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;
import ca.sciencestudio.device.control.event.dispatcher.DeviceEventDispatcher;

/**
 * @author maxweld
 *
 */
public abstract class AbstractDeviceEventPublisher implements DeviceEventPublisher {

	protected List<DeviceEventListener> deviceEventListeners = new ArrayList<DeviceEventListener>();
	protected boolean publishingEvents = false;
	protected long publishedEvents = 0L;
	
	public AbstractDeviceEventPublisher() {
		startPublishingEvents();
	}
	
	protected void publishEvent(final DeviceEvent deviceEvent) {
		if(publishingEvents) {
			DeviceEventDispatcher.dispatchEvent(deviceEventListeners, deviceEvent);
			publishedEvents++;
		}
	}
	
	public boolean isPublishingEvents() {
		return publishingEvents;
	}
	
	public void setPublishingEvents(boolean publishingEvents) {
		if(this.publishingEvents != publishingEvents) {
			this.publishingEvents = publishingEvents;
			if(publishingEvents) { publishedEvents = 0L; }
		}
	}
	
	public void startPublishingEvents() {
		setPublishingEvents(true);
	}
	
	public void stopPublishingEvents() {
		setPublishingEvents(false);
	}
	
	public boolean hasPublishedEvents() {
		return (publishedEvents > 0L);
	}
	
	public long getPublishedEvents() {
		return publishedEvents;
	}
	
	public void addEventListener(DeviceEventListener deviceEventListener) {
		if(!deviceEventListeners.contains(deviceEventListener)) {
			deviceEventListeners.add(deviceEventListener);
		}
	}
	
	public void removeEventListener(DeviceEventListener deviceEventListener) {
		deviceEventListeners.remove(deviceEventListener);
	}
}
