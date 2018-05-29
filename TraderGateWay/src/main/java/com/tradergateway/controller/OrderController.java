package com.tradergateway.controller;

import com.tradergateway.Tools.ConvertBlotter;
import com.tradergateway.Tools.ConvertToModel;
import com.tradergateway.Tools.IntOrder;
import com.tradergateway.model.Blotter;
import com.tradergateway.model.Order;
import com.tradergateway.service.BlotterService;
import com.tradergateway.service.OrderService;
import com.tradergateway.service.ProducerService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by homepppp on 2018/5/24.
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    BlotterService blotterService;
    @Autowired
    ProducerService producerService;

    private ConvertToModel convertToModel = new ConvertToModel();
    private IntOrder intOrder = new IntOrder();
    private ConvertBlotter convertBlotter = new ConvertBlotter();

    private static Logger logger = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "depth",method = RequestMethod.POST)
    public ResponseEntity<JSONArray> getDepth(@RequestBody JSONObject obj){
        JSONArray arr = new JSONArray();
        String product = (String) obj.get("product");
        String period = (String) obj.get("period");
        List<Order> list = orderService.getDepth(product,period);
        List<List<Order>> sepList = new ArrayList<>();
        List<Order> temp = new ArrayList<>();

        for(int j =0;j<list.size();j++){
            if(temp.size()==0 || list.get(j).getPrice()==temp.get(0).getPrice()) {
                temp.add(list.get(j));
                logger.info(list.get(j).getPrice()+"  "+temp.get(0).getPrice());
            }
            else{
                sepList.add(temp);
                logger.info(sepList.get(0).get(0).getPrice());
                temp = new ArrayList<>();
                temp.add(list.get(j));
            }
        }
        logger.info("--------------------------------------------------");
        if(temp.size()>0)
            sepList.add(temp);
        logger.info("--------------------------------------------------");
        //logger.info("finish sepList size =" + sepList.get(0).size());
        int i =0;
        for(;i<sepList.size();i++){
            if(sepList.get(i).get(0).getSide()==1)
                break;
        }
        logger.info("--------------------------------------------------");
        for(int a = 0;a<sepList.size();a++){
            int sum =0;
            for(int b =0;b<sepList.get(a).size();b++){
                sum+=sepList.get(a).get(b).getQuantity();
            }
            JSONObject deal = new JSONObject();
            deal.put("price",sepList.get(a).get(0).getPrice());
            if(a<i)
                deal.put("Sell Vol",sum);
            else
                deal.put("Buy Vol",sum);
            arr.add(deal);
        }
        return new ResponseEntity<JSONArray>(arr,HttpStatus.OK);
    }

    @RequestMapping(value = "depth/order",method = RequestMethod.POST)
    public ResponseEntity<Void> sendOrder(@RequestBody JSONObject obj){
        Destination destination = new ActiveMQQueue("mytest.queue");
        producerService.sendMessage(destination,obj);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "depth/stopOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getStopOrder(@RequestBody JSONObject obj){
        Destination destination = new ActiveMQQueue("mytest.queue");
        producerService.sendMessage(destination,obj);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "depth/marketOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getMarketOrder(@RequestBody JSONObject obj){
        Destination destination = new ActiveMQQueue("mytest.queue");
        producerService.sendMessage(destination,obj);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "depth/limitOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getLimitOrder(@RequestBody JSONObject obj){
        Destination destination = new ActiveMQQueue("mytest.queue");
        producerService.sendMessage(destination,obj);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
