package com.brokergateway.repository;

import com.brokergateway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by homepppp on 2018/5/24.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "select * from orders where product =:product and period =:period and status = 1 and broker=:broker" +
            " order by price DESC,order_time ASC", nativeQuery = true)
    List<Order> getDepth(@Param("product") String product, @Param("period") String period, @Param("broker") String broker);

    @Modifying
    @Transactional
    @Query(value = "update orders set status = 0 where order_id =:orderId", nativeQuery = true)
    void deleteOrder(@Param("orderId") int orderId);

    @Modifying
    @Transactional
    @Query(value = "update orders set quantity =:quantity where order_id =:orderId", nativeQuery = true)
    void updateOrderQuantity(@Param("quantity") int quantity, @Param("orderId") int orderId);

    @Modifying
    @Transactional
    @Query(value = "update orders set status = 0 where trader =:trader and order_time =:time", nativeQuery = true)
    void cancelOrder(@Param("trader") String trader, @Param("time") String time);

    @Query(value = "select new map(p.period,p.product,p.broker) from Order p group by p.period,p.product,p.broker")
    List<Map<String, Object>> getAllProducts();

    @Query(value = "select new map(p.side,p.price,sum(p.quantity)) from Order p where p.product=:product and p.period=:period and p.broker=:broker and p.status = 1 group by " +
            "p.price,p.side,p.quantity order by p.price desc")
    List<Map<String, Object>> getOrderedDepth(@Param("product") String product, @Param("period") String period, @Param("broker") String broker);
}