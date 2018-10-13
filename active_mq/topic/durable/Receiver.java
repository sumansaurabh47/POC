package jms.topic.durable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class Receiver {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Topic destination = null;
	private MessageConsumer consumer = null;
	static int messages = 0;
	final static int MESSAGES_TO_CONSUME = 15;

	public void connnection() {
		try {
			factory = new org.apache.activemq.ActiveMQConnectionFactory("tcp://localhost:61616");
			connection = factory.createConnection();
			connection.setClientID("Suman-1");
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic("test_topic");
			//consumer = session.createConsumer(destination);
			consumer = session.createDurableSubscriber(destination, "durable-1");
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void getMessage() throws JMSException {
		System.out.println("getMessage");
		TextMessage message = (TextMessage) this.consumer.receive();
			messages++;
		System.out.println("Receive: " + message.getText());
	}
	
	public void stopConsumer() {
		try {
			this.connection.stop();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Start");
		Receiver receiver = new Receiver();
		receiver.connnection();
		do {
			receiver.getMessage();
			
		} while (messages < MESSAGES_TO_CONSUME);

		receiver.stopConsumer();

		System.out.println("end");
		System.exit(0);
	}
}