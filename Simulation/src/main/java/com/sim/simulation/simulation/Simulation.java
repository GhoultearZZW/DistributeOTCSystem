package com.sim.simulation.simulation;

import com.sim.simulation.Tools.JsonToProtocol;
import com.sim.simulation.Tools.RandomJson;
import net.sf.json.JSONObject;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component
public class Simulation {
    @Autowired
    private JmsTemplate jmsTemplate;

    private JsonToProtocol jsonToProtocol = new JsonToProtocol();
    private RandomJson randomJson = new RandomJson();

    @Scheduled(fixedDelay = 2000)
    public void sendLimitOrder() throws Exception{

        Destination destination = new ActiveMQQueue("order.queue");

        JSONObject obj = randomJson.getLimitOrder();

        String str = jsonToProtocol.convertToString(obj);
        jmsTemplate.convertAndSend(destination,str);
    }

    @Scheduled(fixedDelay = 3000)
    public void sendMarketOrder() throws Exception{
        Destination destination = new ActiveMQQueue("order.queue");

        JSONObject obj = randomJson.getMarketOrder();
        String str = jsonToProtocol.convertToString(obj);
        jmsTemplate.convertAndSend(destination,str);
    }

    @Scheduled(fixedDelay = 5000)
    public void sendStopOrder() throws Exception{
        Destination destination = new ActiveMQQueue("order.queue");

        JSONObject obj = randomJson.getStopOrder();
        String str = jsonToProtocol.convertToString(obj);
        jmsTemplate.convertAndSend(destination,str);
    }
}
