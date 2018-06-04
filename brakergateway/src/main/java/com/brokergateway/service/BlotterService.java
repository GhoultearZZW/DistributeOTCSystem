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

    public void saveBlotter(Blotter blotter){
        blotterRepository.saveAndFlush(blotter);
    }

    public List<Blotter> getBlotters(String product,String period){
        return blotterRepository.getBlotter(product,period);
    }

    public List<Blotter> getBlottersByCompany(String company){
        return blotterRepository.getBlotterByCompany(company);
    }

    public List<Blotter> getBlotterByCompanyAsInitiator(String company){
        return blotterRepository.getBlotterByCompanyAsInitiator(company);
    }

    public List<Blotter> getBlotterByCompanyAsCompletion(String company){
        return blotterRepository.getBlotterByCompanyAsCompletion(company);
    }

    public List<Blotter> getAllBlotter(){
        return blotterRepository.getAllBlotter();
    }
}
