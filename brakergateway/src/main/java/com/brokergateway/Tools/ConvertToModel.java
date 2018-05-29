package com.brokergateway.Tools;

import com.brokergateway.model.Order;
import com.brokergateway.model.Trader;
import com.brokergateway.model.TraderOrder;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by homepppp on 2018/5/24.
 */
public class ConvertToModel {

    public TraderOrder convertToInitOrder(Order order){
        TraderOrder traderOrder = new TraderOrder();

        traderOrder.setTradeCompany(order.getTradeCompany());
        traderOrder.setBroker(order.getBroker());
        traderOrder.setOrderTime(order.getOrderTime());
        traderOrder.setQuantity(order.getQuantity());
        traderOrder.setPrice(order.getPrice());
        traderOrder.setOrderType(order.getOrderType());
        traderOrder.setPeriod(order.getPeriod());
        traderOrder.setProduct(order.getProduct());
        traderOrder.setSide(order.getSide());
        traderOrder.setTrader(order.getTrader());
        traderOrder.setRestQuantity(order.getQuantity());

        return traderOrder;
    }

    public Order convertToStopOrder(JSONObject obj){
        Order order = new Order();

        order.setProduct((String)obj.get("product"));
        order.setBroker((String)obj.get("broker"));
        order.setOrderType((String)obj.get("orderType"));
        order.setPeriod((String)obj.get("period"));
        order.setQuantity(obj.getInt("quantity"));
        order.setPrice(obj.getDouble("price"));
        order.setSide(obj.getInt("side"));
        order.setTrader((String)obj.get("trader"));
        order.setStatus(1);

        if(obj.containsKey("orderId")){
            order.setOrderId(obj.getInt("orderId"));
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date  date = new Date();
        order.setOrderTime(df.format(date));

        return order;
    }

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

    public Order convertToMarketOrder(JSONObject obj){
        Order order = new Order();

        order.setProduct((String)obj.get("product"));
        order.setBroker((String)obj.get("broker"));
        order.setOrderType((String)obj.get("orderType"));
        order.setPeriod((String)obj.get("period"));
        order.setQuantity(obj.getInt("quantity"));
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
