/**
 * 
 */
package com.sarker.ayub.jms.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
@Component("messageSender")  
public class MessageSender {
	private static Logger log = Logger.getLogger(MessageSender.class);
	
	@Autowired
	private JmsTemplate jmsTemplate = null;
	

	/**
	 * @param jmsTemplate
	 *            the jmsTemplate to set
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * 
	 */
	public void sendMessage(final String message){  
	    jmsTemplate.send(new MessageCreator(){  
	  
	        public Message createMessage(Session session) throws JMSException {  
	            return session.createTextMessage(message);  
	        }  
	    });  
	}  
}
