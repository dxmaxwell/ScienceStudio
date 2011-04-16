/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SpringDeviceEventSender class.		
 * 
 */
package ca.sciencestudio.device.control.messaging.spring;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventSender;

/**
 * @author maxweld
 *
 */
public class SpringDeviceEventSender implements DeviceEventSender {
	
	private JmsTemplate jmsTemplate;
	private Destination destination;
	private String destinationName;
	
	public void send(DeviceEvent deviceEvent) {
		if(destination != null) {
			jmsTemplate.convertAndSend(destination, deviceEvent);
		}
		else if(destinationName != null) {
			jmsTemplate.convertAndSend(destinationName, deviceEvent);
		}
		else {
			jmsTemplate.convertAndSend(deviceEvent);
		}
	}
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
}
