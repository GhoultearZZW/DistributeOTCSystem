package com.brokergateway.service;

import com.brokergateway.model.Order;
import com.brokergateway.repository.OrderRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by homepppp on 2018/5/24.
 */
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;


    private static Logger logger = Logger.getLogger(OrderService.class);

    public void saveLimitOrder(Order order) {
        if (order.getQuantity() > 0)
            orderRepository.saveAndFlush(order);
    }

    public List<Order> getDepth(String product, String period, String broker) {
        return orderRepository.getDepth(product, period, broker);
    }

    public void deleteOrder(Order order) {
        orderRepository.deleteOrder(order.getOrderId());
    }

    public void updateOrder(Order order) {
        orderRepository.updateOrderQuantity(order.getQuantity(), order.getOrderId());
    }

    public void cancelOrder(String trader, String time) {

    }

    public List<Map<String, Object>> getAllProducts() {
        return orderRepository.getAllProducts();
    }

    public List<Map<String, Object>> getDepthSum(String product, String period, String broker) {
        return orderRepository.getOrderedDepth(product, period, broker);
    }
}
