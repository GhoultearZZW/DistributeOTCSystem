package com.brokergateway.service;

import com.brokergateway.Tools.ConvertBlotter;
import com.brokergateway.Tools.ConvertToModel;
import com.brokergateway.Tools.IntOrder;
import com.brokergateway.model.Blotter;
import com.brokergateway.model.Order;
import com.brokergateway.repository.OrderRepository;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by homepppp on 2018/5/24.
 */
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;


    private static Logger logger = Logger.getLogger(OrderService.class);

    public void saveLimitOrder(Order order){
        if(order.getQuantity()>0)
            orderRepository.saveAndFlush(order);
    }

    public List<Order> getDepth(String product, String period){
        return orderRepository.getDepth(product,period);
    }

    public void deleteOrder(Order order){
        orderRepository.deleteOrder(order.getOrderId());
    }

    public void updateOrder(Order order){
        orderRepository.updateOrderQuantity(order.getQuantity(),order.getOrderId());
    }

    public void cancelOrder(String trader,String time){

    }
}
