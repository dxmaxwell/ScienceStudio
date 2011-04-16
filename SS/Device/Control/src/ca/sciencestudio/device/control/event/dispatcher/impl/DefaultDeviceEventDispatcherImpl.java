/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    DefaultDeviceEventDispatcherImpl class.
 *    
 */
package ca.sciencestudio.device.control.event.dispatcher.impl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.dispatcher.DeviceEventDispatcherImpl;

/**
 * @author maxweld
 *
 */
public class DefaultDeviceEventDispatcherImpl extends Thread implements DeviceEventDispatcherImpl {
	
	private DispatcherThread dispatcherThread  = new DispatcherThread();
	
	private BlockingQueue<DispatcherQueueItem> dispatcherQueue = 
					new LinkedBlockingQueue<DispatcherQueueItem>(1000);
	
	private boolean dispatchingEvents = true;
	private long dispatchedEvents = 0L;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public DefaultDeviceEventDispatcherImpl() {
		dispatcherThread.start();
	}
	
	public void dispatchEvent(List<DeviceEventListener> deviceEventListeners, DeviceEvent deviceEvent) {
		if(dispatchingEvents) {
			if((deviceEvent != null) && (deviceEventListeners != null)) {
				DispatcherQueueItem  deviceEventPublisherQueueItem = 
					new DispatcherQueueItem(deviceEvent, deviceEventListeners);
				
				boolean accepted = dispatcherQueue.offer(deviceEventPublisherQueueItem);
				
				if(!accepted) {
					log.warn("Dispatcher queue is full, dropping device event.");
				}
			}
			else {
				log.warn("Cannot dispatch a null device event.");
			}
		}
	}
	
	public boolean isDispatchingEvents() {
		return this.dispatchingEvents;
	}
	
	public void setDispatchingEvents(boolean dispatchingEvents) {
		if(this.dispatchingEvents != dispatchingEvents) {
			this.dispatchingEvents = dispatchingEvents;
			if(dispatchingEvents) { dispatchedEvents = 0L; }
		}
	}
	
	public void startDispatchingEvents() {
		setDispatchingEvents(true); 
	}

	public void stopDispatchingEvents() {
		setDispatchingEvents(false);
	}
	
	public boolean hasDispatchedEvents() {
		return (dispatchedEvents > 0);
	}
	
	public long getDispatchedEvents() {
		return dispatchedEvents;
	}
	
	protected void doDispatchEvent(DeviceEvent deviceEvent, List<DeviceEventListener> deviceEventListeners) {
		for(DeviceEventListener deviceEventListener : deviceEventListeners) {
			try {
				deviceEventListener.handleEvent(deviceEvent);
			}
			catch (Exception e) {
				log.warn("Exception while dispatching device event.", e);
			}
		}
		dispatchedEvents++;
	}
	
	private class DispatcherThread extends Thread {
		
		public DispatcherThread() {
			setPriority(Thread.MAX_PRIORITY);
			setDaemon(true);
		}

		@Override
		public void run() {
			while(true) {
				try {
					DispatcherQueueItem item = dispatcherQueue.take();
					doDispatchEvent(item.getDeviceEvent(), item.getDeviceEventListeners());
				}
				catch(InterruptedException e) {
					log.warn("Dispatcher thread has been interrupted.", e);
					return;
				}
			}
		}
	}
	
	private class DispatcherQueueItem {
		
		private List<DeviceEventListener> deviceEventListeners;
		private DeviceEvent deviceEvent;
		
		public DispatcherQueueItem(DeviceEvent deviceEvent, List<DeviceEventListener> deviceEventListeners) {
			this.deviceEventListeners = deviceEventListeners;
			this.deviceEvent = deviceEvent;
		}

		public List<DeviceEventListener> getDeviceEventListeners() {
			return deviceEventListeners;
		}
		public DeviceEvent getDeviceEvent() {
			return deviceEvent;
		}
	}
}
