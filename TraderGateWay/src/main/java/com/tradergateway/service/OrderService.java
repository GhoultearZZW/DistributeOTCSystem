package com.tradergateway.service;

import com.tradergateway.model.Order;
import com.tradergateway.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by homepppp on 2018/5/24.
 */
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

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

}
