/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractActiveMQDeviceEventReceiver class.
 * 
 */
package ca.sciencestudio.device.control.messaging.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.messaging.DeviceEventReceiver;

/**
 * @author maxweld
 *
 */
public abstract class AbstractActiveMQDeviceEventReceiver implements MessageListener, DeviceEventReceiver  {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String url;
	private String subject;
	private boolean topic;
	
	protected boolean connected = false;
	protected boolean transacted = false;
	
	protected ActiveMQConnectionFactory connectionFactory;
	protected MessageConsumer messageConsumer;
	protected Connection connection;
	protected Session session;
	
	public AbstractActiveMQDeviceEventReceiver(String subject, boolean topic, String url) {
		this.subject = subject;
		this.topic = topic;
		this.url = url;
	}
	
	public void onMessage(Message message) {
		try {		
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				Serializable object = objectMessage.getObject();
				try {
					receive((DeviceEvent) object);
				}
				catch(ClassCastException e) {
					logger.error("Object class is not DeviceEvent", e);
				}
			}
			catch(JMSException e) {
				logger.error("Object could not be unpackaged from ObjectMessage", e);
			}
		}
		catch(ClassCastException e) {
			logger.error("JMS Message class is not ObjectMessage.", e);
		}
	}
	
	public abstract void receive(DeviceEvent deviceEvent);
	
	public void connect() {
		try {
			if(!connected) {
				connectionFactory = new ActiveMQConnectionFactory(url);
				connection = connectionFactory.createConnection();
				connection.start();
				connected = true;
				session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
				if(this.topic) {
					Topic topic = session.createTopic(subject);
					messageConsumer = session.createConsumer(topic);
				} else {
					Queue queue = session.createQueue(subject);
					messageConsumer = session.createConsumer(queue);
				}
				messageConsumer.setMessageListener(this);
				
				logger.info(this.getClass().getSimpleName() + " waiting for messages on " + url);
			}
		} catch (Exception e) {
			connected = false;
			logger.error(e.getMessage());
		}
	}
	
	public void disconnect() {
		try {
			if(connected) {
				connection.close();
				connected = false;
			}
		} catch (Exception e) {
			connected = false;
			logger.error(e.getMessage());
		}
	}
	
	public String getUrl() {
		return this.url;
	}

	public String getSubject() {
		return this.subject;
	}

	public boolean isTopic() {
		return this.topic;
	}
}
