/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractJmsDeviceEventReceiver class.
 * 
 */
package ca.sciencestudio.device.control.messaging.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.MessageListener;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventReceiver;

/**
 * @author maxweld
 *
 */
public abstract class AbstractJmsDeviceEventReceiver implements MessageListener, DeviceEventReceiver {
	
	protected Log log = LogFactory.getLog(getClass()); 
	
	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage)message;
			receive((DeviceEvent)msg.getObject());
		}
		catch(JMSException e) {
			log.warn(e.getMessage());
		}
		catch(ClassCastException e) {
			log.warn("Received message does not contain a DeviceEvent.");
		}
	}

	public abstract void receive(DeviceEvent deviceEvent);
}
