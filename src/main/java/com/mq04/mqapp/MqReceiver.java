package com.mq04.mqapp;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.jms.MQConnectionFactory;

public class MqReceiver {

    private static final String qManager = "QM1";
    private static final String qName = "QL.01";
    private static final Logger LOGGER = LoggerFactory.getLogger(Mqapp.class);
    private MQQueueManager qMgr;
    private MQQueue queue;
    
    public MQQueue receiveMessage() {
        try {
            MQEnvironment.hostname = "localhost";
            MQEnvironment.port = 1414;
            MQEnvironment.channel = "DEV.APP.SVRCONN";
            MQEnvironment.userID="MQAdmin1";
            MQEnvironment.password = "MQ.1001";
            qMgr = new MQQueueManager(qManager);
            int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF ;
            queue = qMgr.accessQueue(qName, openOptions);

            MQMessage rcvMessage = new MQMessage();
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            queue.get(rcvMessage, gmo);
            String msgText = rcvMessage.toString();
            //System.out.print(msgText);
            LOGGER.info("Received message: \n" + msgText);


            // MQConnectionFactory connectionFactory = new MQConnectionFactory();
            // CamelContext context;
            // context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
            // context.addComponent("wmq", connectionFactory);

                return queue;

        } catch (MQException ex) {
            ex.printStackTrace();
            System.out.println("A WebSphere MQ Error occurred : Completion Code " + ex.completionCode + " Reason Code " + ex.reasonCode);


        } catch (JmsException ex) {
            System.out.println("An IOException occurred whilst writing to the message buffer: " + ex);
        }
        return queue;
    }

    public void disconnect() throws MQException{

            queue.close();
            qMgr.disconnect();

    }
    
}
