package com.tradergateway.repository;

import com.tradergateway.model.Blotter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by homepppp on 2018/5/24.
 */
@Repository
public interface BlotterRepository extends JpaRepository<Blotter,Integer>{

}
