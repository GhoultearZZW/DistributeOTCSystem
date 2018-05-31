package com.tradergateway.repository;

import com.tradergateway.model.TraderOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by homepppp on 2018/5/30.
 */
@Repository
public interface TraderOrderRepository extends JpaRepository<TraderOrder,Integer> {
    @Query(value = "select * from trader_order where trader =:trader",nativeQuery = true)
    List<TraderOrder> getByUsername(@Param("trader")String trader);

    @Query(value = "select * from trader_order where trader =:trader and status = 0",nativeQuery = true)
    List<TraderOrder> getFinishedByUsername(@Param("trader")String trader);

    @Query(value = "select * from trader_order where trader =:trader and status = 1",nativeQuery = true)
    List<TraderOrder> getUnfinishedByUsername(@Param("trader")String trader);

    @Query(value = "select * from trader_order where trader =:trader and status = 2",nativeQuery = true)
    List<TraderOrder> getStoppedByUsername(@Param("trader")String trader);

    @Query(value = "select * from trader_order where trader =:trader and status = 3",nativeQuery = true)
    List<TraderOrder> getCancelledByUsername(@Param("trader")String trader);
}
