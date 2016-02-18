/**
 * 
 */
package com.ayub.sarker.core.jms.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class JMSQueueSender {
	private static Logger log = Logger.getLogger(JMSQueueSender.class);
	private QueueConnectionFactory connectionFactory = null;
	private QueueConnection connection = null;
	private QueueSession session = null;
	private Queue queue = null;
	private QueueSender sender = null;
	private InitialContext context = null;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(
			System.in));
	private String jndiQueueConnectionFactory = null;
	private String jndiQueue = null;

	/**
	 * 
	 */
	public JMSQueueSender(String jndiQueueConnectionFactory, String jndiQueue) {
		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
		this.jndiQueue = jndiQueue;
	}

	public void initializeConnection(Properties props) {
		try {
			context = new InitialContext(props);
			log.debug("Context is  Created");
			connectionFactory = (QueueConnectionFactory) context
					.lookup(jndiQueueConnectionFactory);
			log.debug("QueueConnectionFactory is Created");
			connection = connectionFactory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) context.lookup(jndiQueue);
			log.debug("queue is found");
			sender = session.createSender(queue);
			log.info("JMs Sender is initialized successfully.");
		} catch (NamingException e) {
			log.error("Error in initializing connection: " + e.getMessage());
		} catch (JMSException e) {
			log.error("Error in initializing connection: " + e.getMessage());
		}

	}

	public void deinitializeConnectio() {
		try {
			connection.close();
			log.info("Jndi close susscessfully");
		} catch (JMSException e) {
			log.equals("Error in closing JNDI connection" + e.getMessage());
		}

	}

	public void start() {
		try {
			TextMessage message = session.createTextMessage();
			while (true) {
				System.out.println("Enter Msg, end to terminate:");
				String s = reader.readLine();
				if (s.equals("end"))
					break;
				message.setText(s);
				sender.send(message);
				System.out.println("Message successfully sent.");
			}
		} catch (JMSException e) {
			log.error("Error in sending message" + e.getMessage());
		} catch (IOException e) {
			log.error("Error in sending message" + e.getMessage());
		}

	}

	public static void main(String[] args) {
		PropertyConfigurator
				.configureAndWatch("config/log4j.properties", 2000L);
		
		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "mq://localhost:4848");
		
		// (You may also have to provide security credentials)
		
		JMSQueueSender jmsSender = new JMSQueueSender("myQueueConnectionFactory",
				"myQueue");
		jmsSender.initializeConnection(props);
		jmsSender.start();
		jmsSender.deinitializeConnectio();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.equals("Error in main: " + e.getMessage());
		}
		
		JMSQueueReceiver jmsReceiver = new JMSQueueReceiver("myQueueConnectionFactory",
				"myQueue");
		jmsReceiver.initializeConnection(props);
		jmsReceiver.start();
		jmsReceiver.deinitializeConnectio();
	}
}
