package com.tradergateway.jms;

import net.sf.json.JSONObject;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by homepppp on 2018/5/28.
 */
@Component
public class Consumer {
    /*@JmsListener(destination = "mytest.queue")
    public void receiveQueue(JSONObject text){
       System.out.println("Consumer get:"+ (String)text.get("product"));
    }*/
}
