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
	
	private static final String DEFAULT_SESSION_GID = "0";
	private static final long DEFAULT_SESSION_END_TIME = 0;
	private static final String DEFAULT_ADMINISTRATOR_GID = "0";
	private static final long DEFAULT_PROCESS_MONITOR_WAIT = 2000L;
	private static final long DEFAULT_PROCESS_INITIALIZE_WAIT = 2000L;
	
	protected static final String VALUE_KEY_SESSION_GID = "sessionGid";
	protected static final String VALUE_KEY_SESSION_TIMEOUT = "sessionTimeout";
	protected static final String VALUE_KEY_SESSION_END_TIME = "sessionEndTime";
	protected static final String VALUE_KEY_ADMINISTRATOR_GID = "administratorGid";
	protected static final String VALUE_KEY_SESSION_START = "start";
	protected static final String VALUE_KEY_SESSION_STOP = "stop";
	
	private long processInitializeWait = DEFAULT_PROCESS_INITIALIZE_WAIT;
	
	private String sessionGid = DEFAULT_SESSION_GID;
	private long sessionEndTime = DEFAULT_SESSION_END_TIME;
	private String administratorGid = DEFAULT_ADMINISTRATOR_GID;
	
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
		valueMap.put(VALUE_KEY_SESSION_GID, getSessionGid());
		valueMap.put(VALUE_KEY_SESSION_TIMEOUT, getSessionTimeout());
		valueMap.put(VALUE_KEY_SESSION_END_TIME, getSessionEndTime());
		valueMap.put(VALUE_KEY_ADMINISTRATOR_GID, getAdministratorGid());
		return valueMap;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
			if(valueMap.containsKey(VALUE_KEY_SESSION_START)) {
			
				String sessionGid = DEFAULT_SESSION_GID;
				if(valueMap.containsKey(VALUE_KEY_SESSION_GID)) {
					try {
						sessionGid = (String) valueMap.get(VALUE_KEY_SESSION_GID);
					}
					catch(ClassCastException e) {
						log.warn("Set value of " + VALUE_KEY_SESSION_GID + " wrong class, expecting String.");
					}
				}
				
				long sessionEndTime = DEFAULT_SESSION_END_TIME;
				if(valueMap.containsKey(VALUE_KEY_SESSION_END_TIME)) {
					try {
						sessionEndTime = (Long) valueMap.get(VALUE_KEY_SESSION_END_TIME);
					}
					catch(ClassCastException e) {
						log.warn("Set value of " + VALUE_KEY_SESSION_END_TIME + " wrong class, expecting Long.");
					}
				}
				
				String administratorGid = DEFAULT_ADMINISTRATOR_GID;
				if(valueMap.containsKey(VALUE_KEY_ADMINISTRATOR_GID)) {
					try {
						administratorGid = (String) valueMap.get(VALUE_KEY_ADMINISTRATOR_GID);
					}
					catch(ClassCastException e) {
						log.warn("Set value of " + VALUE_KEY_ADMINISTRATOR_GID + " wrong class, expecting String.");
					}
				}
				
				if((sessionEndTime > 0L) &&
						(!DEFAULT_SESSION_GID.equalsIgnoreCase(sessionGid)) &&
						(!DEFAULT_ADMINISTRATOR_GID.equalsIgnoreCase(administratorGid))) {
					startProcess(sessionGid, sessionEndTime, administratorGid);
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
	
	protected void startProcess(String sessionGid, long sessionEndTime, String administratorGid) {
		if(!isProcessRunning()) {
			long now = new Date().getTime();
			if(sessionEndTime > now) {
				setSessionGid(sessionGid);
				setSessionEndTime(sessionEndTime);
				setAdministratorGid(administratorGid);
				startProcess();
			}
		}
	}
	
	public String getSessionGid() {
		return sessionGid;
	}
	protected void setSessionGid(String sessionGid) {
		this.sessionGid = sessionGid;
	}

	public long getSessionEndTime() {
		return sessionEndTime;
	}
	protected void setSessionEndTime(long sessionEndTime) {
		this.sessionEndTime = sessionEndTime;
	}
	
	public String getAdministratorGid() {
		return administratorGid;
	}
	public void setAdministratorGid(String administratorGid) {
		this.administratorGid = administratorGid;
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
							setSessionGid(DEFAULT_SESSION_GID);
							setSessionEndTime(DEFAULT_SESSION_END_TIME);
							setAdministratorGid(DEFAULT_ADMINISTRATOR_GID);
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
