package br.com.application.activeMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerRoute {
	
	private static final String ROUTE = "tcp://0.0.0.0:61616";
	
	public Message getMessage(String queue) throws JMSException {
		ConnectionFactory factory = new ActiveMQConnectionFactory(ROUTE);
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queue);

		MessageConsumer messageConsumer = session.createConsumer(destination);
		Message message = messageConsumer.receive(1000);
		
		session.close();
		messageConsumer.close();
		connection.close();
		
		return message;
	}
}
