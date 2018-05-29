package com.brokergateway.repository;

import com.brokergateway.model.Trader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by homepppp on 2018/5/23.
 */
@Repository
public interface TraderRepository extends JpaRepository<Trader,Integer> {
    @Query("SELECT p FROM Trader p where p.username=:username and p.password=:password")
    Trader getTraderByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
