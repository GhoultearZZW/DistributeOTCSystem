package com.brokergateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.brokergateway.model.broker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface BrokerRepository extends JpaRepository<broker,Integer> {

    @Query(value = "select * from broker where username =:username and password=:password",nativeQuery = true)
    broker getBrokerByUsernameAndPassword(@Param("username")String username,@Param("password")String password);

}
