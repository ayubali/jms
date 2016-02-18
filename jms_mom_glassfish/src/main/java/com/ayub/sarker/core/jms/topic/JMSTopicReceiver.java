/**
 * 
 */
package com.ayub.sarker.core.jms.topic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ayub.sarker.core.jms.queue.JMSQueueReceiver;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class JMSTopicReceiver {
	private static Logger log = Logger.getLogger(JMSTopicReceiver.class);

	private TopicConnectionFactory connectionFactory = null;
	private TopicConnection connection = null;
	private TopicSession session = null;
	private Topic topic = null;
	private TopicSubscriber subscriber = null;
	private InitialContext context = null;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(
			System.in));
	private String jndiTopicConnectionFactory = null;
	private String jndiTopic = null;

	/**
 * 
 */
	public JMSTopicReceiver(String jndiQueueConnectionFactory, String jndiQueue) {
		this.jndiTopicConnectionFactory = jndiQueueConnectionFactory;
		this.jndiTopic = jndiQueue;
	}

	public void initializeConnection(Properties props) {
		try {
			context = new InitialContext(props);
			log.debug("Context is  Created");
			connectionFactory = (TopicConnectionFactory) context
					.lookup(jndiTopicConnectionFactory);
			log.debug("jndiTopicConnectionFactory is Created");
			connection = connectionFactory.createTopicConnection();
			connection.start();
			session = connection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);
			topic = (Topic) context.lookup(jndiTopic);
			log.debug("Topic is found");
			subscriber = session.createSubscriber(topic);
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
		MyTopicListener listener = new MyTopicListener();
		try {
			subscriber.setMessageListener(listener);
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

	public static void main(String[] args) {
		PropertyConfigurator
				.configureAndWatch("config/log4j.properties", 2000L);

		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "mq://localhost:4848");
		JMSTopicReceiver jmsTopicReceiver = new JMSTopicReceiver(
				"topicConnectionFactory", "topic");
		jmsTopicReceiver.initializeConnection(props);
		jmsTopicReceiver.start();
		jmsTopicReceiver.deinitializeConnectio();
	}
}
