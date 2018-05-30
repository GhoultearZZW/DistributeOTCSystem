package com.tradergateway.service;

import com.tradergateway.model.TraderOrder;
import com.tradergateway.repository.TraderOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by homepppp on 2018/5/30.
 */
@Service
public class TraderOrderService {
    @Autowired
    TraderOrderRepository traderOrderRepository;

    public List<TraderOrder> getByUsername(String trader){
        return traderOrderRepository.getByUsername(trader);
    }

    public List<TraderOrder> getFinishedByUsername(String trader){
        return traderOrderRepository.getFinishedByUsername(trader);
    }

    public List<TraderOrder> getUnfinishedByUsername(String trader){
        return traderOrderRepository.getUnfinishedByUsername(trader);
    }

    public List<TraderOrder> getStoppedByUsername(String trader){
        return traderOrderRepository.getStoppedByUsername(trader);
    }
    public List<TraderOrder> getCancelledByUsername(String trader){
        return traderOrderRepository.getCancelledByUsername(trader);
    }
}
