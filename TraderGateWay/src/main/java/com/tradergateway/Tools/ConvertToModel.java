package com.tradergateway.Tools;

import com.tradergateway.model.Order;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by homepppp on 2018/5/24.
 */
public class ConvertToModel {
    /*public Order convertToOrder(JSONObject obj){
        Order order = new Order();

        order.setBroker((String)obj.get("broker"));
        order.setOrderType((String)obj.get("orderType"));
        order.setPeriod((String)obj.get("period"));
        order.setQuantity(Integer.parseInt((String)obj.get("quantity")));
        order.setPrice(Double.parseDouble((String)obj.get("price")));
        order.setSide(Integer.parseInt((String)obj.get("side")));
        order.setTrader((String)obj.get("trader"));
        order.setTradeCompany((String)obj.get("tradeCompamy"));

        if(obj.containsKey("orderId")){
            order.setOrderId(obj.getInt("orderId"));
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        order.setOrderTime(df.format(date));

        return order;
    }*/

    public Order convertToLimitOrder(JSONObject obj){
        Order order = new Order();

        order.setProduct((String)obj.get("product"));
        order.setBroker((String)obj.get("broker"));
        order.setOrderType((String)obj.get("orderType"));
        order.setPeriod((String)obj.get("period"));
        order.setQuantity(obj.getInt("quantity"));
        order.setPrice(obj.getDouble("price"));
        order.setSide(obj.getInt("side"));
        order.setTrader((String)obj.get("trader"));
        order.setTradeCompany((String)obj.get("tradeCompany"));
        order.setStatus(1);

        if(obj.containsKey("orderId")){
            order.setOrderId(obj.getInt("orderId"));
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        order.setOrderTime(df.format(date));

        return order;
    }
}
