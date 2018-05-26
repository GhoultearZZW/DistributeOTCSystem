package com.tradergateway.service;

import com.tradergateway.repository.BlotterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by homepppp on 2018/5/24.
 */
@Service
public class BlotterService {
    @Autowired
    BlotterRepository blotterRepository;
}
