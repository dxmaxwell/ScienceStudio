/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractDeviceEventListenerMessageSender class.
 * 
 */
package ca.sciencestudio.device.control.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;
import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageType;

/**
 * @author maxweld
 *
 */
public abstract class AbstractDeviceEventListenerMessageSender implements DeviceEventListener {

	protected Log log = LogFactory.getLog(getClass());
	
	public AbstractDeviceEventListenerMessageSender(List<DeviceEventPublisher> eventPublishers) {
		for(DeviceEventPublisher eventPublisher : eventPublishers) {
			eventPublisher.addEventListener(this);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void handleEvent(DeviceEvent deviceEvent) {
		
		HashMap<String,Serializable> valueMap = new HashMap<String,Serializable>();
		
		switch(deviceEvent.getDeviceEventType()) {
		
			case VALUE_CHANGE:
				if(deviceEvent.getValue() instanceof Map) {
					try {
						valueMap.putAll((Map<String,Serializable>) deviceEvent.getValue());
					}
					catch(ClassCastException e) {
						log.info("DeviceEvent value is not Map<String,Serializable>.");
					}
				}
				else if(deviceEvent.getValue() instanceof Serializable){
					valueMap.put("value", (Serializable) deviceEvent.getValue());
				}
				else {
					log.warn("DeviceEvent value is not Serializable.");
				}
				break;
		
			case CONNECTIVITY_CHANGE:
				valueMap.put("connectivity", deviceEvent.getStatus().toString());
				break;
			
			case ALARM_CHANGE:
				valueMap.put("alarmStatus", deviceEvent.getAlarmStatus());
				valueMap.put("alarmSeverity", deviceEvent.getAlarmSeverity());
				break;
			
			default:
				log.warn("DeviceEvent type is not handled.");
		}
		
		DeviceMessage<HashMap<String,Serializable>> deviceMessage = 
						new DeviceMessage<HashMap<String,Serializable>>();
		
		deviceMessage.setValue(valueMap);
		deviceMessage.setDeviceId(deviceEvent.getDeviceId());
		deviceMessage.setTimestamp(deviceEvent.getTimestamp());
		deviceMessage.setDeviceMessageType(DeviceMessageType.VALUE_CHANGE);
		send(deviceMessage);
	}
	
	public abstract void send(DeviceMessage<?> deviceMessage);
}
