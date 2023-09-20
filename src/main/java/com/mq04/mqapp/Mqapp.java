package com.mq04.mqapp;

import org.springframework.jms.JmsException;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.mq.MQQueue;


@RestController
@SpringBootApplication
public class Mqapp {

	private static final Logger LOGGER = LoggerFactory.getLogger(Mqapp.class);
	public static void main(String[] args) {
		SpringApplication.run(Mqapp.class, args);
		//DistributeOrderDSL distributeOrderDSL = new DistributeOrderDSL();
		// try {
		//    distributeOrderDSL.connect();
		// } catch (Exception e) {
		//    e.printStackTrace();
		// }
	}
	@GetMapping("recv")
	String recv(){
		try {
			LOGGER.info("Invoking the MqReceiver Object...");
			MqReceiver mqreceiver = new MqReceiver();
			//mqreceiver.connect();
			mqreceiver.receiveMessage();
			//mqreceiver.closeConnection();
			return "Receive OK";
		} catch (JmsException e) {
			e.printStackTrace();
			return "MqReceiver FAILED - No message was received";
		}
	}

}


@Component(value = "BasketApplication")
class DistributeOrderDSL {
	public MQQueue queue;
	private static final Logger LOGGER = LoggerFactory.getLogger(Mqapp.class);

   	public void connect() throws Exception {
	
      CamelContext context = new DefaultCamelContext();
      try {
			LOGGER.info("Invoking the BasketApplication Object...");
         context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
               from("jms:QL.01")
                  //.split(xpath("//order[@product='soaps']/items"))
                  //.to("stream:out");
               
                .to("file:src/main/resources/order/");
            }
         });
         context.start();

		 
         ProducerTemplate orderProducerTemplate = context.createProducerTemplate();
         //InputStream orderInputStream = new FileInputStream(ClassLoader.getSystemClassLoader().getResource("order.xml").getFile());
		MqReceiver mqreceiver = new MqReceiver();
		queue = mqreceiver.receiveMessage();
         orderProducerTemplate.sendBody("jms:DistributeOrderDSL2", mqreceiver);
      } finally {
         context.stop();
      }
   }

}


		// MqReceiver mqreceiver = new MqReceiver();
		// queue = mqreceiver.receiveMessage();
		// JmsEndpoint endpoint = new JmsEndpoint();
		// orderProducerTemplate.sendBody(endpoint, queue);


