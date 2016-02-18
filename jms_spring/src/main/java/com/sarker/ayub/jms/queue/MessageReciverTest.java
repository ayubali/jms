/**
 * 
 */
package com.sarker.ayub.jms.queue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mortbay.log.Log;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class MessageReciverTest {
	private static Logger log = Logger.getLogger(MessageReciverTest.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator
		.configureAndWatch("config/log4j.properties", 2000L);
		
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("JMSTemplate-Receiver-Context.xml");
		ctx.refresh();

		while (true) {
			Log.info("-------->waiting for messsage");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error("Error in receiving: "+ e.getMessage());
			}
		}
	}

}
