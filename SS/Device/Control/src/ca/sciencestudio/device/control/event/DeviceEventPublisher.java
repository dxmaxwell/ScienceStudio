/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    DeviceEventPublisher interface.
 */
package ca.sciencestudio.device.control.event;

import ca.sciencestudio.device.control.event.DeviceEventListener;

/**
 * @author maxweld
 *
 */
public interface DeviceEventPublisher {
	
	public boolean isPublishingEvents();
	public void setPublishingEvents(boolean publishingEvents);
	
	public void startPublishingEvents();
	public void stopPublishingEvents();
	
	public boolean hasPublishedEvents();
	public long getPublishedEvents();
		
	public void addEventListener(DeviceEventListener deviceEventListener);
	public void removeEventListener(DeviceEventListener deviceEventListener);
}
