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
        String product = (String)obj.get("product");
        String period = (String)obj.get("period");
        String broker = (String)obj.get("broker");
        System.out.println("product ="+product+"   period="+period+"    broker="+broker);
        if(product.equals("") && period.equals("")){
            List<TraderOrder> list = traderOrderService.getAllOrder(broker);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        List<TraderOrder> list = traderOrderService.getTraderOrderByCompany(product,period,broker);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/finished",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getFinishedOrders(@RequestBody JSONObject obj){
        String product = (String)obj.get("product");
        String period = (String)obj.get("period");
        String broker = (String)obj.get("broker");
        if(product.equals("") && period.equals("")){
            List<TraderOrder> list = traderOrderService.getFinishedAll(broker);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        List<TraderOrder> list = traderOrderService.getFinishedOrders(product,period,broker);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/unfinished",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getUnfinishedOrders(@RequestBody JSONObject obj){
        String product = (String)obj.get("product");
        String period = (String)obj.get("period");
        String broker = (String)obj.get("broker");
        if(product.equals("") && period.equals("")){
            List<TraderOrder> list = traderOrderService.getUnfinishedAll(broker);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        List<TraderOrder> list = traderOrderService.getUnfinishedOrders(product,period,broker);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }


    @RequestMapping(value = "/orders/stopped",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getStoppedOrders(@RequestBody JSONObject obj){
        String product = (String)obj.get("product");
        String period = (String)obj.get("period");
        String broker = (String)obj.get("broker");
        if(product.equals("") && period.equals("")){
            List<TraderOrder> list = traderOrderService.getStoppedAll(broker);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        List<TraderOrder> list = traderOrderService.getStoppedOrders(product,period,broker);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }


    @RequestMapping(value = "/orders/canceled",method = RequestMethod.POST)
    public ResponseEntity<List<TraderOrder>> getCanceledOrders(@RequestBody JSONObject obj){
        String product = (String)obj.get("product");
        String period = (String)obj.get("period");
        String broker = (String)obj.get("broker");
        if(product.equals("") && period.equals("")){
            List<TraderOrder> list = traderOrderService.getCanceledAll(broker);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        List<TraderOrder> list = traderOrderService.getCanceledOrders(product,period,broker);
        return new ResponseEntity<List<TraderOrder>>(list,HttpStatus.OK);
    }
}
