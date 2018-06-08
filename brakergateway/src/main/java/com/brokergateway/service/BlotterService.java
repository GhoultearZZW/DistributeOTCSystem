package com.brokergateway.service;

import com.brokergateway.model.Blotter;
import com.brokergateway.repository.BlotterRepository;
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

    public void saveBlotter(Blotter blotter) {
        blotterRepository.saveAndFlush(blotter);
    }

    public List<Blotter> getBlotters(String product, String period) {
        return blotterRepository.getBlotter(product, period);
    }

    public List<Blotter> getBlottersByCompany(String company, String broker) {
        return blotterRepository.getBlotterByCompany(company, broker);
    }

    public List<Blotter> getBlotterByCompanyAsInitiator(String company, String broker) {
        return blotterRepository.getBlotterByCompanyAsInitiator(company, broker);
    }

    public List<Blotter> getBlotterByCompanyAsCompletion(String company, String broker) {
        return blotterRepository.getBlotterByCompanyAsCompletion(company, broker);
    }

    public List<Blotter> getAllBlotter(String broker) {
        return blotterRepository.getAllBlotter(broker);
    }
}
