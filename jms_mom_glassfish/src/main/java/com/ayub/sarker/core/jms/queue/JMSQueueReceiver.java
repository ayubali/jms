/**
 * 
 */
package com.ayub.sarker.core.jms.queue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
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
public class JMSQueueReceiver {
	private static Logger log = Logger.getLogger(JMSQueueReceiver.class);
	private QueueConnectionFactory connectionFactory = null;
	private QueueConnection connection = null;
	private QueueSession session = null;
	private Queue queue = null;
	private QueueReceiver receiver = null;
	private InitialContext context = null;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(
			System.in));
	private String jndiQueueConnectionFactory = null;
	private String jndiQueue = null;

	/**
	 * 
	 */
	public JMSQueueReceiver(String jndiQueueConnectionFactory, String jndiQueue) {
		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
		this.jndiQueue = jndiQueue;
	}

	public void initializeConnection(Properties prop) {
		try {
			context = new InitialContext(prop);
			log.debug("Context is  Created");
			connectionFactory = (QueueConnectionFactory) context
					.lookup(jndiQueueConnectionFactory);
			log.debug("QueueConnectionFactory is Created");
			connection = connectionFactory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) context.lookup("myQueue");
			log.debug("queue is found");
			receiver = session.createReceiver(queue);
			log.info("JMS Receiver is initialized successfully.");
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
		MyOueueListener listener = new MyOueueListener();
		try {
			receiver.setMessageListener(listener);
			while (true) {
				Thread.sleep(1000);
				log.debug("waiting for message...");
			}

		} catch (JMSException e) {
			log.error("Error in getting message: " + e.getMessage());
		} catch (InterruptedException e) {
			log.error("Error in getting message: " + e.getMessage());
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator
				.configureAndWatch("config/log4j.properties", 2000L);

		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "mq://localhost:4848");
		JMSQueueReceiver jmsReceiver = new JMSQueueReceiver("myQueueConnectionFactory",
				"myQueue");
		jmsReceiver.initializeConnection(props);
		jmsReceiver.start();
		jmsReceiver.deinitializeConnectio();

	}

}
