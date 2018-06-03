package com.brokergateway.Controller;

import com.brokergateway.model.TraderOrder;
import com.brokergateway.service.TraderOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.omg.PortableServer.RequestProcessingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by homepppp on 2018/5/29.
 */
@RestController
public class OrderController {

    @Autowired
    TraderOrderService traderOrderService;

    @RequestMapping(value = "/orders",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getOrders(@RequestBody JSONObject obj){
        String firm = (String)obj.get("Company");
        List<TraderOrder> list = traderOrderService.getTraderOrderByCompany(firm);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/finished",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getFinishedOrders(@RequestBody JSONObject obj){
        String firm = (String)obj.get("Company");
        List<TraderOrder> list = traderOrderService.getFinishedOrders(firm);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/unfinished",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getUnfinishedOrders(@RequestBody JSONObject obj){
        String firm = (String)obj.get("Company");
        List<TraderOrder> list = traderOrderService.getUnfinishedOrders(firm);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }


    @RequestMapping(value = "/orders/stopped",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getStoppedOrders(@RequestBody JSONObject obj){
        String firm = (String)obj.get("Company");
        List<TraderOrder> list = traderOrderService.getStoppedOrders(firm);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }


    @RequestMapping(value = "/orders/canceled",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getCanceledOrders(@RequestBody JSONObject obj){
        String firm = (String)obj.get("Company");
        List<TraderOrder> list = traderOrderService.getCanceledOrders(firm);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }
}
