package jms.queue.destination.non_exclusive;

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
	
	private String json = "{\"deviceType\": \"TestingDeviceType1\",	\"sentTimestamp\": \"2017-06-22T22:16:57.041Z\",	\"messageId\": \"5c52ad19-c296-4795-8724-f297ea0b239f\",	\"siteName\": \"ULTRAMAR CORNER STORE\",	\"timeZone\": \"America/St_Johns\",	\"source\": \"36480\",	\"deviceId\": \"00001\",	\"localId\": \"1.0\",	\"organizationId\": [\"0\", \"80001\"],	\"pin\": {		\"location\": {			\"lon\": -53.2795729,			\"lat\": 47.5810626		}	},	\"payload\": {		\"sendOrder\": 956.0,		\"utcOffsetSeconds\": -1.657621382959E9,		\"tankId\": \"1\",		\"sampleTimestamp\": \"2070-01-01T08:40:00.000Z\",		\"sampleCount\": 1.0,		\"state\": \"0\",		\"fuelHeight\": 38.643917083740234,		\"waterHeight\": 0.0,		\"temperature00\": 60.00028610229492,		\"temperature01\": 60.00028610229492,		\"temperature02\": 60.00028610229492,		\"temperature03\": 60.00028610229492,		\"temperature04\": 60.00028610229492,		\"temperature05\": 60.00028610229492	},	\"sourceType\": \"PTC\",	\"siteId\": \"100001\",	\"operation\": \"probe-sample-event\",	\"brand\": \"ULTRAMAR\",	\"timestamp\": \"2017-06-22T22:16:57.055Z\"}";

	public void createConnection() throws JMSException{
		factory = new org.apache.activemq.ActiveMQConnectionFactory("tcp://localhost:61616");
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue("test_queue");
		producer = session.createProducer(destination);
	}
	
	public void sendMessage(String groupId) {
		try {
			TextMessage textMessage = session.createTextMessage("Hello   " + groupId);
			textMessage.setStringProperty("siteId", "100001");
			textMessage.setStringProperty("operation", "probe-sample-event");
			textMessage.setJMSMessageID("123456");
			textMessage.setStringProperty("JMSXGroupID", groupId);
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
			for(int i=1; i<=4; i++){
				String gid = i%2==0 ? "B" : "A";
				sender.sendMessage(gid);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
