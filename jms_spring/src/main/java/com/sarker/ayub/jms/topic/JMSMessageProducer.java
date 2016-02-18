/**
 * 
 */
package com.sarker.ayub.jms.topic;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.sarker.ayub.jms.queue.MessageSender;

/**
 * @author ayub.ali
 * 
 * @date 18 Feb 2016
 */
@Component
public class JMSMessageProducer {
	private static Logger log = Logger.getLogger(JMSMessageProducer.class);

	protected static final String MESSAGE_COUNT = "MessageNumber";

	@Autowired
	private JmsTemplate jmsTemplate = null;

	private int messageCount = 100;

	@PostConstruct
	public void generateMessages() throws JMSException {

		for (int i = 0; i < messageCount; i++) {
			final int index = i;
			final String text = "Message number is " + i + ".";
			jmsTemplate.send(new MessageCreator() {

				public Message createMessage(Session session)
						throws JMSException {
					TextMessage message = session.createTextMessage(text);
					message.setIntProperty(MESSAGE_COUNT, index);
					log.info(" sending message: " + text);
					return message;
				}
			});
		}
	}
	
	public static void main(String[] args) {
		PropertyConfigurator
		.configureAndWatch("config/log4j.properties", 2000L);
		
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load("JMSTemplate-Producer-Context.xml");
		context.refresh();
	}

}
