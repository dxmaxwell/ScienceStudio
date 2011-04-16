/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		BeamlineSession class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.lang.Thread;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;
import ca.sciencestudio.device.control.component.impl.ProcessDevice;

/**
 * @author maxweld
 *
 */
public class BeamlineSession extends ProcessDevice implements DeviceEventListener {
	
	private static final long DEFAULT_PROCESS_INITIALIZE_WAIT = 2000L;
	private static final long DEFAULT_PROCESS_MONITOR_WAIT = 2000L;
	
	protected static final String VALUE_KEY_SESSION_ID = "sessionId";
	protected static final String VALUE_KEY_SESSION_TIMEOUT = "sessionTimeout";
	protected static final String VALUE_KEY_SESSION_END_TIME = "sessionEndTime";
	protected static final String VALUE_KEY_SESSION_START = "start";
	protected static final String VALUE_KEY_SESSION_STOP = "stop";
	
	private long processInitializeWait = DEFAULT_PROCESS_INITIALIZE_WAIT;
	
	private int sessionId = 0;
	private long sessionEndTime = 0;
	
	public BeamlineSession(String id, List<DeviceEventPublisher> eventPublishers) {
		super(id);
		for(DeviceEventPublisher eventPublisher : eventPublishers) {
			eventPublisher.addEventListener(this);
		}
		startPublishingEvents();
		new BeamlineSessionThread();
	}
	
	public Object getValue() {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_SESSION_ID, getSessionId());
		valueMap.put(VALUE_KEY_SESSION_TIMEOUT, getSessionTimeout());
		valueMap.put(VALUE_KEY_SESSION_END_TIME, getSessionEndTime());
		return valueMap;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
			if(valueMap.containsKey(VALUE_KEY_SESSION_START)) { 
			
				int sessionId = 0;
				if(valueMap.containsKey(VALUE_KEY_SESSION_ID)) {
					try {
						sessionId = (Integer) valueMap.get(VALUE_KEY_SESSION_ID);
					}
					catch(ClassCastException e) {
						log.warn("Set value of " + VALUE_KEY_SESSION_ID + " wrong class, expecting Integer.");
					}
				}
				
				long sessionEndTime = 0;
				if(valueMap.containsKey(VALUE_KEY_SESSION_END_TIME)) {
					try {
						sessionEndTime = (Long) valueMap.get(VALUE_KEY_SESSION_END_TIME);
					}
					catch(ClassCastException e) {
						log.warn("Set value of " + VALUE_KEY_SESSION_ID + " wrong class, expecting Long.");
					}
				}
				
				if((sessionId > 0) && (sessionEndTime >0)) {
					startProcess(sessionId, sessionEndTime);
				}
			}
			else if(valueMap.containsKey(VALUE_KEY_SESSION_STOP)) {
				stopProcess();
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value wrong class, expecting Map<String,Object>.");
		}
	}
	
	protected void startProcess(int sessionId, long sessionEndTime) { 
		if(!isProcessRunning()) {
			long now = new Date().getTime(); 
			if(sessionEndTime > now) {
				setSessionId(sessionId);
				setSessionEndTime(sessionEndTime);
				startProcess();
			}
		}
	}
	
	public int getSessionId() {
		return sessionId;
	}
	protected void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public long getSessionEndTime() {
		return sessionEndTime;
	}
	protected void setSessionEndTime(long sessionEndTime) {
		this.sessionEndTime = sessionEndTime;
	}
	
	public int getSessionTimeout() {
		long now = new Date().getTime();
		long timeout = sessionEndTime - now;
		if(timeout < 0) { return 0; }
		return (int)(timeout / 1000L);
	}
	
	public long getProcessInitializeWait() {
		return processInitializeWait;
	}
	public void setProcessInitializeWait(long processInitializeWait) {
		this.processInitializeWait = processInitializeWait;
	}
	public void setProcessInitializeWaitSeconds(int processInitializeWait) {
		this.processInitializeWait = processInitializeWait * 1000L;
	}
	
	public void handleEvent(DeviceEvent deviceEvent) {
		
		switch(deviceEvent.getDeviceEventType()) {
			case GET_VALUE:
				if(id.equals(deviceEvent.getDeviceId())) {
					publishValue();
				}
				break;
				
			case SET_VALUE:
				if(id.equals(deviceEvent.getDeviceId())) {
					setValue(deviceEvent.getValue());
				}
				break;
			
			case VALUE_CHANGE:
				break;
			
			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;
		}
	}
 
	
	private class BeamlineSessionThread extends Thread {
			
		public BeamlineSessionThread() {
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			
			boolean processRunning = false;
			
			while(true) {
				
				try {
					
					if(processRunning != isProcessRunning()) {
						processRunning = isProcessRunning();
						
						if(processRunning) {
							Thread.sleep(getProcessInitializeWait());
							continue;
						}
						else {
							setSessionId(0);
							setSessionEndTime(0);
							publishValue();
						}	
					}
					else {
						
						if(processRunning) {
							if(getSessionTimeout() == 0) {
								stopProcess();
							}
							else {
								publishValue();
							}
						}
					}
					
					sleep(DEFAULT_PROCESS_MONITOR_WAIT); 
				}
				catch (InterruptedException  e) {
					continue;
				}
			}
		}
	}
}
