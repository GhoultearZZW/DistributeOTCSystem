package com.brokergateway.jms;

import com.brokergateway.Tools.ProtocolToJson;
import net.sf.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by homepppp on 2018/5/29.
 */
@Component
public class Consumer {
    private ExecOrder execOrder = new ExecOrder();
    private ProtocolToJson protocolToJson = new ProtocolToJson();
    public static Consumer consumer;

    /*@JmsListener(destination = "mytest.queue")
    public void receiverQueue(JSONObject obj){
        consumer = this;
        String orderType = (String)obj.get("orderType");
        if(orderType.equals("LimitOrder"))
            execOrder.execLimitOrder(obj);
        else if(orderType.equals("StopOrder"))
            execOrder.execStopOrder(obj);
        else if(orderType.equals("MarketOrder"))
            execOrder.execMarketOrder(obj);
        else if(orderType.equals("CancelOrder"))
            execOrder.execCancelOrder(obj);
    }*/

    @JmsListener(destination = "order.queue")
    public void receiveQueue(String str){
        JSONObject obj = new JSONObject();
        obj = protocolToJson.convertToJson(str);
        consumer = this;
        String orderType = (String)obj.get("orderType");
        if(orderType.equals("LimitOrder"))
            execOrder.execLimitOrder(obj);
        else if(orderType.equals("StopOrder"))
            execOrder.execStopOrder(obj);
        else if(orderType.equals("MarketOrder"))
            execOrder.execMarketOrder(obj);
        else if(orderType.equals("CancelOrder"))
            execOrder.execCancelOrder(obj);
    }

    @JmsListener(destination = "test.queue")
    public void receiveTest(String str){
        JSONObject obj = new JSONObject();
        obj = protocolToJson.convertToJson(str);
        System.out.println(obj);
    }
}
