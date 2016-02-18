/**
 * 
 */
package com.ayub.sarker.core.jms.queue;

/**
 * @author ayub.ali
 * 
 * @date 14 Feb 2016
 */

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class AppJMSQueue {

	public static void main(String a[]) throws Exception {

		// Commands to create Queue
		// asadmin --port 4848 create-jms-resource --restype javax.jms.Queue
		// TestQueue
		// asadmin --port 4848 create-jms-resource --restype
		// javax.jms.QueueConnectionFactory TestQueueConnectionFactory

		String msg = "Hello from remote JMS Client";

		AppJMSQueue test = new AppJMSQueue();

		System.out.println("==============================");
		System.out.println("Sending message to Queue");
		System.out.println("==============================");
		System.out.println();
		test.sendMessage2Queue(msg);
		System.out.println();
		System.out.println("==============================");
		System.exit(0);
	}

	private void sendMessage2Queue(String msg) throws Exception {

		// Provide the details of remote JMS Client
		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "mq://localhost:4848");

		// Create the initial context for remote JMS server
		InitialContext cntxt = new InitialContext(props);
		System.out.println("Context Created");

		// JNDI Lookup for QueueConnectionFactory in remote JMS Provider
		QueueConnectionFactory qFactory = (QueueConnectionFactory) cntxt
				.lookup("myQueueConnectionFactory");

		// Create a Connection from QueueConnectionFactory
		Connection connection = qFactory.createConnection();
		System.out.println("Connection established with JMS Provide ");

		// Initialise the communication session
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Create the message
		TextMessage message = session.createTextMessage();
		message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		message.setText(msg);

		// JNDI Lookup for the Queue in remote JMS Provider
		Queue queue = (Queue) cntxt.lookup("myQueue");

		// Create the MessageProducer for this communication
		// Session on the Queue we have
		MessageProducer mp = session.createProducer(queue);

		// Send the message to Queue
		mp.send(message);
		System.out.println("Message Sent: " + msg);

		// Make sure all the resources are released
		mp.close();
		session.close();
		cntxt.close();

	}

}