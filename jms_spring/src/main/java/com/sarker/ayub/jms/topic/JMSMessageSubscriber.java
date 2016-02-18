/**
 * 
 */
package com.sarker.ayub.jms.topic;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author ayub.ali
 * 
 * @date 18 Feb 2016
 */
@Component("jMSMessageSubscriber")
public class JMSMessageSubscriber implements MessageListener {
	private static Logger log = Logger.getLogger(JMSMessageSubscriber.class);

	@Autowired
	private AtomicInteger counter = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		try {
			int messageCount = message
					.getIntProperty(JMSMessageProducer.MESSAGE_COUNT);

			if (message instanceof TextMessage) {
				TextMessage tm = (TextMessage) message;
				log.info("Received message: {" + tm.getText() + "}  value={"
						+ messageCount + "}");
			}
		} catch (JMSException jmsException) {
			log.error(jmsException.getMessage());
		}
	}
	
	public static void main(String[] args) {
		PropertyConfigurator
		.configureAndWatch("config/log4j.properties", 2000L);
		
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load("JMSTemplate-Subscriber-Context.xml");
		context.refresh();
	}
}
