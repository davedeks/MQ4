package com.mq04.mqapp;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ibm.mq.MQQueue;

@Component
public class MqReceiverRoute extends RouteBuilder {
    //private MQQueueManager qMgr;
    //private MQQueue queue;
    private static final Logger LOGGER = LoggerFactory.getLogger(Mqapp.class);    

    public void configure() throws Exception {

        MqReceiver mqReceiver = new MqReceiver();
        MQQueue queue = mqReceiver.receiveMessage();

        //CamelContext context = new DefaultCamelContext();
        //added that line after some checks but didnt help
        //context.addComponent("http", queue);

        LOGGER.info("Received message: \n" + queue.toString());
        //System.out.println("DEKS Receive msg from JmsListener 303: " + queue.toString());

        from("jms:QL.01")
            //.routeId("generate-route")
            //.transform(constant("HELLO from Camel!"))
            .to("log:IBM MQ Message Recieved in Camel EndPoint")
            .to("file:src/main/resources/order/?fileName=datafile.txt&charset=utf-8");    
    }

}
