/**
 * 
 */
package com.ayub.sarker.core.jms.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class MyOueueListener implements MessageListener{
	private static Logger log = Logger.getLogger(MyOueueListener.class);
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;

		try {
			System.out
					.println("following message is received:" + msg.getText());
			log.info("Message received: " + msg.getText());
		} catch (JMSException e) {
			log.error("Error in getting message: " + e.getMessage());
		}

	}
}
