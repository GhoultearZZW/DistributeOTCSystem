package com.brokergateway.service;

import com.brokergateway.model.Trader;
import com.brokergateway.model.TraderOrder;
import com.brokergateway.repository.TraderOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<TraderOrder> getTraderOrderByCompany(String company){
        return traderOrderRepository.getTradeOrder(company);
    }

    public List<TraderOrder> getFinishedOrders(String company){
        return traderOrderRepository.getFinishedOrder(company);
    }

    public List<TraderOrder> getUnfinishedOrders(String company){
        return traderOrderRepository.getUnfinishedOrder(company);
    }

    public List<TraderOrder> getStoppedOrders(String company){
        return traderOrderRepository.getStoppedOrder(company);
    }

    public List<TraderOrder> getCanceledOrders(String company){
        return traderOrderRepository.getCanceledOrder(company);
    }
}
