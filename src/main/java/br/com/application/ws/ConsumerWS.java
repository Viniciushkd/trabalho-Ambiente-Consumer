package br.com.application.ws;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.application.activeMQ.ConsumerRoute;
import br.com.application.camel.RouteConsumer;
import br.com.application.entity.Person;

@RestController
public class ConsumerWS {

	private ConsumerRoute consumerRoute = new ConsumerRoute();
	private RouteConsumer routeConsumer = new RouteConsumer();

	@RequestMapping("/")
	public String producer() {
		return "<h1>Consumer Web Service</h1>"
		 		+ "Consumir Queue Objeto:<br>"
		 		+ "<b>WS:</b> \\consumerPerson<br>"
		 		+ "<b>Exemplo:</b> http://localhost:9500/consumerPerson<br><br>"
		 		+ "Consumir Queue Texto:<br>"
		 		+ "<b>WS:</b> \\consumerMessage<br>"
		 		+ "<b>Exemplo:</b> http://localhost:9500/consumerMessage<br><br>"
		 		+ "Consumir Queue Route File:<br>"
		 		+ "<b>WS:</b> \\startRouteConsumer";
	}

	@RequestMapping("/consumerPerson")
	public String producerPerson() {
		Message message;
		StringBuilder msg = new StringBuilder();
		try {
			do {
				message = consumerRoute.getMessage("Object");
				if (message != null) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					Person person = new Person();
					person = (Person) objectMessage.getObject();
					msg.append("---------- Object<br>");
					msg.append(objectMessage.getObject() + "<br>");
					msg.append(message.getJMSMessageID() + " - Name: " + person.getName() + "; Age: " + person.getAge() + "<br><br>");
				}
			} while (message != null);
		} catch (JMSException e) {
			e.printStackTrace();
		}

		return msg.toString();
	}
	
	@RequestMapping("/consumerMessage")
	public String producerMessage() {
		Message message;
		StringBuilder msg = new StringBuilder();
		try {
			do {
				message = consumerRoute.getMessage("Message");
				if (message != null) {
					TextMessage textMessage = (TextMessage) message;
					msg.append("---------- Text<br>");
					msg.append(message.getJMSMessageID() + " - " + textMessage.getText() + "<br><br>");
				}
			} while (message != null);
		} catch (JMSException e) {
			e.printStackTrace();
		}

		return msg.toString();
	}

	@RequestMapping("/startRouteConsumer")
	public String producerMessageFile() {
		try {
			routeConsumer.consumerQueueFile();
			return "Queue enviada para o arquivo: 'C:\\routeOut'";
		} catch (Exception e) {
			e.printStackTrace();
			return "Falha ao recuperar Queue";
		}
	}
}
