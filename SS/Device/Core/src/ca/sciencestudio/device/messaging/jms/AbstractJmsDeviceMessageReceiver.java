/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractJmsDeviceMessageReceiver class.
 * 
 */
package ca.sciencestudio.device.messaging.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.device.messaging.DeviceMessage;
import ca.sciencestudio.device.messaging.DeviceMessageReceiver;

/**
 * @author maxweld
 *
 */
public abstract class AbstractJmsDeviceMessageReceiver implements MessageListener, DeviceMessageReceiver {
	
	protected Log log = LogFactory.getLog(getClass()); 
	
	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage)message;
			receive((DeviceMessage<?>)msg.getObject());
		}
		catch(JMSException e) {
			log.warn(e.getMessage());
		}
		catch(ClassCastException e) {
			log.warn("Received message does not contain a DeviceMessage.");
		}
	}

	public abstract void receive(DeviceMessage<?> deviceMessage);
}
