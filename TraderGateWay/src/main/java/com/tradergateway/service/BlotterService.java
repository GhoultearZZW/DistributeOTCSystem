package com.tradergateway.service;

import com.tradergateway.model.Blotter;
import com.tradergateway.repository.BlotterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by homepppp on 2018/5/24.
 */
@Service
public class BlotterService {
    @Autowired
    BlotterRepository blotterRepository;

    public void saveBlotter(Blotter blotter){
        blotterRepository.saveAndFlush(blotter);
    }

    public List<Blotter> getBlotters(String product,String period,String broker){
        return blotterRepository.getBlotter(product,period,broker);
    }
}
