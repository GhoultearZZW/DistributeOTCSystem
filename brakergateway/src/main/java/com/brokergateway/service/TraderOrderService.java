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

    public void saveTraderOrder(TraderOrder traderOrder) {
        traderOrderRepository.saveAndFlush(traderOrder);
    }

    public void updateRestQuantity(String trader, String date, int quantity) {
        traderOrderRepository.updateRestQuantity(quantity, date, trader);
    }

    public void updatePrice(String trader, String date, double price) {
        traderOrderRepository.updatePrice(price, date, trader);
    }

    public void finishOrder(String trader, String date) {
        traderOrderRepository.finishOrder(date, trader);
    }

    public void cancelOrder(int orderId) {
        traderOrderRepository.cancelOrder(orderId);
    }

    public TraderOrder getTraderOrderById(int orderId) {
        return traderOrderRepository.getTraderOrder(orderId);
    }

    public List<TraderOrder> getTraderOrderByCompany(String product, String period, String broker) {
        return traderOrderRepository.getTradeOrder(product, period, broker);
    }

    public List<TraderOrder> getFinishedOrders(String product, String period, String broker) {
        return traderOrderRepository.getFinishedOrder(product, period, broker);
    }

    public List<TraderOrder> getUnfinishedOrders(String product, String period, String broker) {
        return traderOrderRepository.getUnfinishedOrder(product, period, broker);
    }

    public List<TraderOrder> getStoppedOrders(String product, String period, String broker) {
        return traderOrderRepository.getStoppedOrder(product, period, broker);
    }

    public List<TraderOrder> getCanceledOrders(String product, String period, String broker) {
        return traderOrderRepository.getCanceledOrder(product, period, broker);
    }

    public List<TraderOrder> getAllOrder(String broker) {
        return traderOrderRepository.getOrder(broker);
    }

    public List<TraderOrder> getFinishedAll(String broker) {
        return traderOrderRepository.getFinishedAll(broker);
    }

    public List<TraderOrder> getUnfinishedAll(String broker) {
        return traderOrderRepository.getUnfinishedAll(broker);
    }

    public List<TraderOrder> getStoppedAll(String broker) {
        return traderOrderRepository.getStoppedAll(broker);
    }

    public List<TraderOrder> getCanceledAll(String broker) {
        return traderOrderRepository.getCanceledAll(broker);
    }
}
