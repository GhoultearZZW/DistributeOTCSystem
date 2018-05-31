package com.brokergateway.jms;

import net.sf.json.JSONObject;
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
    public static Consumer consumer;

    @JmsListener(destination = "mytest.queue")
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
    }
}
