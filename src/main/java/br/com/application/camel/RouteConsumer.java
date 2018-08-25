package br.com.application.camel;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class RouteConsumer {

	private static final String ROUTE = "tcp://0.0.0.0:61616";
	private static final String QUEUE = "File";
	private static final String FILE = "C:\\routeOut";
	
	public void consumerQueueFile() throws Exception {
		CamelContext ctx = new DefaultCamelContext();

		ConnectionFactory factory = new ActiveMQConnectionFactory(ROUTE);
		ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(factory));
		
		ctx.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("jms:queue:" + QUEUE).to("stream:out").to("file:" + FILE);
			}
		});
		
		ctx.start();
		Thread.sleep(5 * 60 * 1000);
		ctx.stop();
	}
}

