package jms.topic.nondurable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Sender {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	public Sender() {

	}
	
	public void createConnection() throws JMSException{
		factory = new org.apache.activemq.ActiveMQConnectionFactory("tcp://localhost:61616");
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic("test_topic");
		producer = session.createProducer(destination);
	}
	
	public void sendMessage(int i) {
		try {
			TextMessage textMessage = session.createTextMessage("Hello   " + i);
			textMessage.setJMSMessageID("M" + System.currentTimeMillis());
			producer.send(textMessage);
			System.out.println("Sent: " + textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Sender sender = new Sender();
		try {
			sender.createConnection();
			for(int i=1; i<=10; i++){
				sender.sendMessage(i);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
