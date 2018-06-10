package com.tradergateway.service;

import com.tradergateway.model.Company;
import com.tradergateway.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public int getCredit(String name){
        Company company = companyRepository.getCompany(name);
        return company.getCredit();
    }

}
