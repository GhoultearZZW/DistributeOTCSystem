package com.tradergateway.controller;

import com.tradergateway.model.TraderOrder;
import com.tradergateway.service.TraderOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by homepppp on 2018/5/30.
 */
@RestController
public class TraderOrderController {

    @Autowired
    private TraderOrderService traderOrderService;

    @RequestMapping(value = "/user/{username}/orders", method = RequestMethod.GET)
    public ResponseEntity<List<TraderOrder>> getOrders(@PathVariable("username") String username) {
        List<TraderOrder> list = traderOrderService.getByUsername(username);
        return new ResponseEntity<List<TraderOrder>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}/orders/finished", method = RequestMethod.GET)
    public ResponseEntity<List<TraderOrder>> getFinishedOrders(@PathVariable("username") String username) {
        List<TraderOrder> list = traderOrderService.getFinishedByUsername(username);
        return new ResponseEntity<List<TraderOrder>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}/orders/unfinished", method = RequestMethod.GET)
    public ResponseEntity<List<TraderOrder>> getUnfinishedOrders(@PathVariable("username") String username) {
        List<TraderOrder> list = traderOrderService.getUnfinishedByUsername(username);
        return new ResponseEntity<List<TraderOrder>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}/orders/stopped", method = RequestMethod.GET)
    public ResponseEntity<List<TraderOrder>> getStoppedOrders(@PathVariable("username") String username) {
        List<TraderOrder> list = traderOrderService.getStoppedByUsername(username);
        return new ResponseEntity<List<TraderOrder>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}/orders/cancelled", method = RequestMethod.GET)
    public ResponseEntity<List<TraderOrder>> getCancelledOrders(@PathVariable("username") String username) {
        List<TraderOrder> list = traderOrderService.getCancelledByUsername(username);
        return new ResponseEntity<List<TraderOrder>>(list, HttpStatus.OK);
    }
}
