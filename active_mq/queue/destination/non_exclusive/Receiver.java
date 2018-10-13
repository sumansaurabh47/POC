package jms.queue.destination.non_exclusive;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Receiver {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;
	static int messages = 0;
	final static int MESSAGES_TO_CONSUME = 10;

	public void connnection() {
		try {
			factory = new org.apache.activemq.ActiveMQConnectionFactory("tcp://localhost:61616");
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("test_queue?consumer.exclusive=false"); //exclusive=false
			consumer = session.createConsumer(destination, "JMSXGroupID='A'");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void getMessage() throws JMSException {
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
		Receiver receiver = new Receiver();
		receiver.connnection();
		do {
			receiver.getMessage();
			
		} while (messages < MESSAGES_TO_CONSUME);

		receiver.stopConsumer();

		System.out.println("end");
	}
}