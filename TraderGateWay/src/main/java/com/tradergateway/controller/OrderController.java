package com.tradergateway.controller;

import com.tradergateway.Tools.CheckDeal;
import com.tradergateway.Tools.ConvertToModel;
import com.tradergateway.model.Order;
import com.tradergateway.service.OrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by homepppp on 2018/5/24.
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    ConvertToModel convertToModel = new ConvertToModel();

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

    @RequestMapping(value = "depth/limitOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getLimitOrder(@RequestBody JSONObject obj){
        Order order = convertToModel.convertToLimitOrder(obj);
        //orderService.saveLimitOrder(order);

        //check if deal if finished
        // ...
        // ...
        // ...
        List<Order> list = orderService.getDepth(order.getProduct(),order.getPeriod());

        logger.info("info"+list.size());

        if(list.size()==0){
            orderService.saveLimitOrder(order);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        List<List<Order>> sepList = new ArrayList<>();
        List<Order> temp = new ArrayList<>();
        for(int j =0;j<list.size();j++){
            if(temp.size()==0 || list.get(j).getPrice()==temp.get(0).getPrice())
                temp.add(list.get(j));
            else{
                sepList.add(temp);
                temp = new ArrayList<>();
                temp.add(list.get(j));
            }
        }
        sepList.add(temp);
        logger.info("finish sepList size =" + sepList.size());

        int i =0;
        for(;i<sepList.size();i++){
            if(sepList.get(i).get(0).getSide()==1)
                break;
        }
        logger.info("i="+i);
        //the position i is the first postion to buy
        /*if(order.getSide()==0 && (order.getPrice() > sepList.get(i).get(0).getPrice()||i == sepList.size()))
            orderService.saveLimitOrder(order);*/
        if(order.getSide() == 0){
            logger.info("do minus for Sell");
            for(int a = i;a<sepList.size();a++){
                if(sepList.get(a).get(0).getPrice()>=order.getPrice()){
                    int b=0;
                    while(order.getQuantity()>0 && b<sepList.get(a).size()){
                        int num = sepList.get(a).get(b).getQuantity();
                        if(num > order.getQuantity()){
                            System.out.println(num-order.getQuantity());
                            sepList.get(a).get(b).setQuantity(num-order.getQuantity());
                            orderService.updateOrder(sepList.get(a).get(b));
                            order.setQuantity(0);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        else{
                            order.setQuantity(order.getQuantity()-num);
                            orderService.deleteOrder(sepList.get(a).get(b));
                        }
                        b++;
                    }
                }
                else{
                    orderService.saveLimitOrder(order);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
            }
            orderService.saveLimitOrder(order);
        }
        else if(order.getSide() == 1){
            logger.info("do minus for Buy");
            for(int a =i-1;a>=0;a--){
                if(sepList.get(a).get(0).getPrice()<=order.getPrice()){
                    int b = 0;
                    while(order.getQuantity()>0 && b<sepList.get(a).size()){
                        int num = sepList.get(a).get(b).getQuantity();
                        if(num > order.getQuantity()){
                            order.setQuantity(0);
                            sepList.get(a).get(b).setQuantity(num-order.getQuantity());
                            orderService.updateOrder(sepList.get(a).get(b));
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        else{
                            order.setQuantity(order.getQuantity()-num);
                            orderService.deleteOrder(sepList.get(a).get(b));
                        }
                        b++;
                    }
                }
                else{
                    orderService.saveLimitOrder(order);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
            }
            orderService.saveLimitOrder(order);
        }


        return new ResponseEntity<Void>(HttpStatus.OK);

    }

}
