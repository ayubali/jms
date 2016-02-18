/**
 * 
 */
package com.sarker.ayub.jms.queue;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class MyMessageListener implements MessageListener {
	private static Logger log = Logger.getLogger(MyMessageListener.class);

	public void onMessage(Message msg) {
		TextMessage message = (TextMessage) msg;
		try {
			log.info("Received message: " + msg);
		} catch (Exception e) {
			log.info("Error in  receiving msg: " + e.getMessage());

		}
	}

}
