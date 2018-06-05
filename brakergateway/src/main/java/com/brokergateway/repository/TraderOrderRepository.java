package com.brokergateway.repository;

import com.brokergateway.model.TraderOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

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

    @Modifying
    @Transactional
    @Query(value = "update trader_order set status = 3 where order_id =:id",nativeQuery = true)
    void cancelOrder(@Param("id")int orderId);

    @Query(value = "select * from trader_order where order_id =:id",nativeQuery = true)
    TraderOrder getTraderOrder(@Param("id")Integer id);

    @Query(value = "select * from trader_order where product =:product and period =:period and broker=:broker",nativeQuery = true)
    List<TraderOrder> getTradeOrder(@Param("product")String product,@Param("period")String period,@Param("broker")String broker);

    @Query(value = "select * from trader_order where product =:product and period =:period and broker=:broker and status = 0",nativeQuery = true)
    List<TraderOrder> getFinishedOrder(@Param("product")String product,@Param("period")String period,@Param("broker")String broker);

    @Query(value = "select * from trader_order where  product =:product and period =:period and broker=:broker and status = 1",nativeQuery = true)
    List<TraderOrder> getUnfinishedOrder(@Param("product")String product,@Param("period")String period,@Param("broker")String broker);

    @Query(value = "select * from trader_order where  product =:product and period =:period and broker=:broker and status = 2",nativeQuery = true)
    List<TraderOrder> getStoppedOrder(@Param("product")String product,@Param("period")String period,@Param("broker")String broker);

    @Query(value = "select * from trader_order where  product =:product and period =:period and broker=:broker and status = 3",nativeQuery = true)
    List<TraderOrder> getCanceledOrder(@Param("product")String product,@Param("period")String period,@Param("broker")String broker);

    @Query(value = "select * from trader_order where broker =:broker",nativeQuery = true)
    List<TraderOrder> getOrder(@Param("broker")String broker);

    @Query(value = "select * from trader_order where broker=:broker and status = 0",nativeQuery = true)
    List<TraderOrder> getFinishedAll(@Param("broker")String broker);

    @Query(value = "select * from trader_order where broker =:broker and status =1",nativeQuery = true)
    List<TraderOrder> getUnfinishedAll(@Param("broker")String broker);

    @Query(value = "select * from trader_order where broker=:broker and status =2",nativeQuery = true)
    List<TraderOrder> getStoppedAll(@Param("broker")String broker);

    @Query(value="select * from trader_order where broker=:broker and status =3",nativeQuery = true)
    List<TraderOrder> getCanceledAll(@Param("broker")String broker);
}
