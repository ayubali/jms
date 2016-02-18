/**
 * 
 */
package com.sarker.ayub.jms.chart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author ayub.ali
 * 
 * @date 18 Feb 2016
 */
public class BasicChat implements MessageListener {

	private JmsTemplate jmsTemplate = null;
	private Topic chatTopic;
	private static String userId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 * @throws JMSException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JMSException, IOException {
		PropertyConfigurator
				.configureAndWatch("config/log4j.properties", 2000L);
		if (args.length != 1) {
			System.out.println("User name is required.");
		} else {
			userId = args[0];
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"jms-spring-config.xml");
			BasicChat basicChat = (BasicChat) context.getBean("chatBean");

			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) basicChat.jmsTemplate
					.getConnectionFactory();
			TopicConnection topicConnection = topicConnectionFactory
					.createTopicConnection();
			basicChat.publish(topicConnection, basicChat.chatTopic, userId);
			basicChat
					.subscribe(topicConnection, basicChat.chatTopic, basicChat);

		}

	}

	/**
	 * @param topicConnection
	 * @param chatTopic2
	 * @param userId2
	 * @throws JMSException
	 */
	private void subscribe(TopicConnection topicConnection, Topic chatTopic2,
			BasicChat basicChat) throws JMSException {
		TopicSession session = topicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		TopicSubscriber ts = session.createSubscriber(chatTopic);
		ts.setMessageListener(basicChat);

	}

	/**
	 * @param topicConnection
	 * @param chatTopic2
	 * @param userId2
	 * @throws JMSException
	 * @throws IOException
	 */
	private void publish(TopicConnection topicConnection, Topic chatTopic,
			String userId2) throws JMSException, IOException {

		TopicSession session = topicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		TopicPublisher tp = session.createPublisher(chatTopic);
		topicConnection.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		while (true) {
			String message = reader.readLine();
			if (message.equals("exit")) {
				topicConnection.close();
				System.exit(0);
			} else {
				TextMessage textMessage = session.createTextMessage();
				textMessage.setText("\n[" + userId2 + " :" + message + "]");
				tp.publish(textMessage);
			}

		}

	}

}
