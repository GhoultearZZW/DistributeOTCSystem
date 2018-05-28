package com.tradergateway.controller;

import com.tradergateway.Tools.ConvertBlotter;
import com.tradergateway.Tools.ConvertToModel;
import com.tradergateway.Tools.IntOrder;
import com.tradergateway.model.Blotter;
import com.tradergateway.model.Order;
import com.tradergateway.service.BlotterService;
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
    @Autowired
    BlotterService blotterService;

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

    @RequestMapping(value = "depth/stopOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getStopOrder(@RequestBody JSONObject obj){
        Order order = convertToModel.convertToStopOrder(obj);

        double limitPrice = order.getPrice();

        List<Order> list = orderService.getDepth(order.getProduct(),order.getPeriod());

        List<List<Order>> sepList = intOrder.getIntOrder(list);

        int i =0;
        for(;i<sepList.size();i++){
            if(sepList.get(i).get(0).getSide()==1)
                break;
        }

        int quantity = order.getQuantity();
        if(order.getSide() == 1){
            for(int a =i-1;a>=0;a--){
                if(sepList.get(a).get(0).getPrice()>=limitPrice){
                    order.setQuantity(quantity);
                    order.setStatus(2);
                    orderService.saveLimitOrder(order);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
                for(int b =0;b<sepList.get(a).size();b++){
                    Order nowOrder = sepList.get(a).get(b);
                    if(nowOrder.getQuantity()>quantity){
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),quantity,nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        nowOrder.setQuantity(nowOrder.getQuantity()-quantity);
                        orderService.updateOrder(nowOrder);
                        // call user to pay ...
                        // . . .
                        // . . .
                        return new ResponseEntity<Void>(HttpStatus.OK);
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),nowOrder.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        quantity-=nowOrder.getQuantity();
                        orderService.deleteOrder(nowOrder);
                    }
                }
            }
        }
        else if(order.getSide()==0){
            for(int a = i;a<sepList.size();a++){
                if(sepList.get(a).get(0).getPrice()<=limitPrice){
                    order.setQuantity(quantity);
                    order.setStatus(2);
                    orderService.saveLimitOrder(order);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
                for(int b =0;b<sepList.get(a).size();b++){
                    Order nowOrder = sepList.get(a).get(b);
                    if(nowOrder.getQuantity()>quantity){
                        nowOrder.setQuantity(nowOrder.getQuantity()-quantity);
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),quantity,nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        orderService.updateOrder(nowOrder);
                        // call user to pay ...
                        // . . .
                        // . . .
                        return new ResponseEntity<Void>(HttpStatus.OK);
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),nowOrder.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        quantity-=nowOrder.getQuantity();
                        orderService.deleteOrder(nowOrder);
                    }
                }
            }
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "depth/marketOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getMarketOrder(@RequestBody JSONObject obj){
        Order order = convertToModel.convertToMarketOrder(obj);

        List<Order> list = orderService.getDepth(order.getProduct(),order.getPeriod());

        List<List<Order>> sepList = intOrder.getIntOrder(list);

        int i =0;
        for(;i<sepList.size();i++){
            if(sepList.get(i).get(0).getSide()==1)
                break;
        }

        int quantity = order.getQuantity();
        if(order.getSide() == 1){
            for(int a =i-1;a>=0;a--){
                for(int b =0;b<sepList.get(a).size();b++){
                    Order nowOrder = sepList.get(a).get(b);
                    if(nowOrder.getQuantity()>quantity){
                        nowOrder.setQuantity(nowOrder.getQuantity()-quantity);
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),quantity,nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        orderService.updateOrder(nowOrder);
                        // call user to pay ...
                        // . . .
                        // . . .
                        return new ResponseEntity<Void>(HttpStatus.OK);
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),nowOrder.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        quantity-=nowOrder.getQuantity();
                        orderService.deleteOrder(nowOrder);
                    }
                }
            }
        }
        else if(order.getSide()==0){
            for(int a = i;a<sepList.size();a++){
                for(int b =0;b<sepList.get(a).size();b++){
                    Order nowOrder = sepList.get(a).get(b);
                    if(nowOrder.getQuantity()>quantity){
                        nowOrder.setQuantity(nowOrder.getQuantity()-quantity);
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),quantity,nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        orderService.updateOrder(nowOrder);
                        // call user to pay ...
                        // . . .
                        // . . .
                        return new ResponseEntity<Void>(HttpStatus.OK);
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),nowOrder.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        quantity-=nowOrder.getQuantity();
                        orderService.deleteOrder(nowOrder);
                    }
                }
            }
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "depth/limitOrder",method = RequestMethod.POST)
    public ResponseEntity<Void> getLimitOrder(@RequestBody JSONObject obj){
        Order order = convertToModel.convertToLimitOrder(obj);

        List<Order> list = orderService.getDepth(order.getProduct(),order.getPeriod());


        if(list.size()==0){
            orderService.saveLimitOrder(order);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        List<List<Order>> sepList = intOrder.getIntOrder(list);

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
                        Order nowOrder = sepList.get(a).get(b);
                        int num = nowOrder.getQuantity();
                        if(num > order.getQuantity()){
                            nowOrder.setQuantity(num-order.getQuantity());
                            Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),order.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                            blotterService.saveBlotter(blotter);
                            orderService.updateOrder(nowOrder);

                            order.setQuantity(0);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        else{
                            int nowQuantity = order.getQuantity()-num;
                            logger.info("nowQuantity"+nowQuantity);
                            Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),num,nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                            blotterService.saveBlotter(blotter);
                            order.setQuantity(nowQuantity);
                            orderService.deleteOrder(nowOrder);
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
                        Order nowOrder = sepList.get(a).get(b);
                        int num = nowOrder.getQuantity();
                        if(num > order.getQuantity()){
                            nowOrder.setQuantity(num-order.getQuantity());
                            orderService.updateOrder(nowOrder);
                            Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),order.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                            blotterService.saveBlotter(blotter);
                            order.setQuantity(0);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        }
                        else{
                            order.setQuantity(order.getQuantity()-num);
                            Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),num,nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                            blotterService.saveBlotter(blotter);
                            orderService.deleteOrder(nowOrder);
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
