package com.tradergateway.service;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * Created by homepppp on 2018/5/28.
 */
@Service("producer")
public class ProducerService {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void sendMessage(Destination destination, final JSONObject message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    public void send(Destination destination, final String str) {
        jmsTemplate.convertAndSend(destination, str);
    }
}
