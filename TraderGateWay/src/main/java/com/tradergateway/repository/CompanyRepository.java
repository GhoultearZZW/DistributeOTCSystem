package com.tradergateway.repository;

import com.tradergateway.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {

    @Query(value = "select p from Company p where p.companyName =:name")
    Company getCompany(@Param("name")String name);
}
