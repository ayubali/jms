/**
 * 
 */
package com.sarker.ayub.jms.queue;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class MessageSenderTest {
	public static void main(String[] args) {
		PropertyConfigurator
		.configureAndWatch("config/log4j.properties", 2000L);
		
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load("JMSTemplate-Sender-Context.xml");
		context.refresh();
		
		MessageSender sender = context.getBean("messageSender", MessageSender.class);
		sender.sendMessage("hi julekha khatun");
	}
}
