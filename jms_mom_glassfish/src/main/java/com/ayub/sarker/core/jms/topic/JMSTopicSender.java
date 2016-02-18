/**
 * 
 */
package com.ayub.sarker.core.jms.topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ayub.sarker.core.jms.queue.JMSQueueSender;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */
public class JMSTopicSender {
	private static Logger log = Logger.getLogger(JMSTopicSender.class);
	private TopicConnectionFactory connectionFactory = null;
	private TopicConnection connection = null;
	private TopicSession session = null;
	private Topic topic = null;
	private TopicPublisher publisher = null;
	private InitialContext context = null;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(
			System.in));
	private String jndiTopicConnectionFactory = null;
	private String jndiTopic = null;

	/**
	 * 
	 */
	public JMSTopicSender(String jndiQueueConnectionFactory, String jndiQueue) {
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
			publisher = session.createPublisher(topic);
			log.info("JMS Sender is initialized successfully.");
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
				publisher.send(message);
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

		JMSTopicSender jmsSender = new JMSTopicSender("topicConnectionFactory",
				"topic");
		jmsSender.initializeConnection(props);
		jmsSender.start();
		jmsSender.deinitializeConnectio();
	}

}
