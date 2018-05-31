package com.brokergateway.service;

import com.brokergateway.model.TraderOrder;
import com.brokergateway.repository.TraderOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by homepppp on 2018/5/29.
 */
@Service
public class TraderOrderService {
    @Autowired
    TraderOrderRepository traderOrderRepository;

    public void saveTraderOrder(TraderOrder traderOrder){
        traderOrderRepository.saveAndFlush(traderOrder);
    }

    public void updateRestQuantity(String trader,String date,int quantity){
        traderOrderRepository.updateRestQuantity(quantity,date,trader);
    }

    public void updatePrice(String trader,String date,double price){
        traderOrderRepository.updatePrice(price,date,trader);
    }

    public void finishOrder(String trader,String date){
        traderOrderRepository.finishOrder(date,trader);
    }

    public void cancelOrder(int orderId){
        traderOrderRepository.cancelOrder(orderId);
    }

    public TraderOrder getTraderOrderById(int orderId){
        return traderOrderRepository.getTraderOrder(orderId);
    }
}
