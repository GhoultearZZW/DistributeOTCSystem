package com.tradergateway.repository;

import com.tradergateway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by homepppp on 2018/5/24.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{
    @Query(value = "select * from orders where product =:product and period =:period and status = 1 and broker =:broker" +
            " order by price DESC,order_time ASC",nativeQuery = true)
    List<Order> getDepth(@Param("product")String product,@Param("period")String period,@Param("broker")String broker);

    @Modifying
    @Transactional
    @Query(value = "update orders set status = 0 where order_id =:orderId",nativeQuery = true)
    void deleteOrder(@Param("orderId")int orderId);

    @Modifying
    @Transactional
    @Query(value = "update orders set quantity =:quantity where order_id =:orderId",nativeQuery = true)
    void updateOrderQuantity(@Param("quantity")int quantity,@Param("orderId")int orderId);
}
