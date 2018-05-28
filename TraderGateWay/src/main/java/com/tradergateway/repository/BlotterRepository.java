package com.tradergateway.repository;

import com.tradergateway.model.Blotter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by homepppp on 2018/5/24.
 */
@Repository
public interface BlotterRepository extends JpaRepository<Blotter,Integer>{
    @Query(value = "select * from blotter where product =:product and period =:period order by deal_time asc",nativeQuery = true)
    List<Blotter> getBlotter(@Param("product")String product,@Param("period")String period);
}
