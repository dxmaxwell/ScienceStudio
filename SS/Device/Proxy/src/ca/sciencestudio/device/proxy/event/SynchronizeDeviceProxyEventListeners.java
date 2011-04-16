/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SynchronizeDeviceProxyEventListeners class.
 *     
 */
package ca.sciencestudio.device.proxy.event;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

import ca.sciencestudio.device.messaging.jms.DelegatingJmsDeviceMessageReceiver;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
public class SynchronizeDeviceProxyEventListeners {

	private DeviceProxyEventPublisher deviceProxyEventPublisher;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public SynchronizeDeviceProxyEventListeners(AbstractMessageListenerContainer messageListenerContainer, long wait) {
		this((DelegatingJmsDeviceMessageReceiver) messageListenerContainer.getMessageListener(), wait);
	}
	
	public SynchronizeDeviceProxyEventListeners(DelegatingJmsDeviceMessageReceiver delegatingJmsDeviceMessageReceiver, long wait) {
		this((DeviceProxyEventPublisher) delegatingJmsDeviceMessageReceiver.getDeviceMessageReceiver(), wait);
	}
	
	public SynchronizeDeviceProxyEventListeners(DeviceProxyEventPublisher deviceProxyEventPublisher, long wait) {
		if(deviceProxyEventPublisher == null) {
			throw new NullPointerException();
		}
		this.deviceProxyEventPublisher = deviceProxyEventPublisher;
		
		if(wait > 0L) {
			new SynchronizeDeviceProxyEventListenersThread(wait);
		} else {
			synchronize(); /* Do not spawn thread if there is no wait. */
		}
	}	
		
	protected void synchronize() {
		Collection<DeviceProxyEventListener> listeners = deviceProxyEventPublisher.getEventListeners();
		for(DeviceProxyEventListener listener : listeners) {
			if(logger.isDebugEnabled()) {
				if(listener instanceof StateMap) {
					logger.debug("Synchronize Device Proxy: " + ((StateMap)listener).getName());
				}
			}
			listener.queryDevice();  // Query devices for current values. //
		}
	}
	
	protected class SynchronizeDeviceProxyEventListenersThread extends Thread {

		private long wait;
		
		public SynchronizeDeviceProxyEventListenersThread(long wait) {
			this.wait = wait;
			setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			try {
				sleep(wait);
				synchronize();
			}
			catch(InterruptedException e) {
				logger.warn("Interrupted while waiting to synchronizes device proxies.");
			}
		}
	}
}
