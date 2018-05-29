package com.brokergateway.repository;

import com.brokergateway.model.TraderOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by homepppp on 2018/5/29.
 */
@Repository
public interface TraderOrderRepository extends JpaRepository<TraderOrder,Integer>{

    @Modifying
    @Transactional
    @Query(value = "update trader_order set status=0 ,rest_quantity=0,deal_price = (price*quantity) where order_time=:orderTime and trader=:trader",nativeQuery = true)
    void finishOrder(@Param("orderTime")String orderTime,@Param("trader")String trader);

    @Modifying
    @Transactional
    @Query(value = "update trader_order set price=:price where order_time=:orderTime and trader=:trader",nativeQuery = true)
    void updatePrice(@Param("price")double price,@Param("orderTime")String orderTime,@Param("trader")String trader);

    @Modifying
    @Transactional
    @Query(value = "update trader_order set rest_quantity=:quantity, deal_price = (price*(quantity-:quantity)) where order_time =:orderTime and trader=:trader",nativeQuery = true)
    void updateRestQuantity(@Param("quantity")int quantity,@Param("orderTime")String orderTime,@Param("trader")String trader);

    @Modifying
    @Transactional
    @Query(value = "update trader_order set status=0 where order_time=:orderTime and trader=:trader",nativeQuery = true)
    void stopOrder(@Param("orderTime")String orderTime,@Param("trader")String trader);
}
