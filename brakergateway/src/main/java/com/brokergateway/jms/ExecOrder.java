package com.brokergateway.jms;

import com.brokergateway.Tools.ConvertBlotter;
import com.brokergateway.Tools.ConvertToModel;
import com.brokergateway.Tools.IntOrder;
import com.brokergateway.model.Blotter;
import com.brokergateway.model.Order;
import com.brokergateway.service.BlotterService;
import com.brokergateway.service.OrderService;
import com.brokergateway.service.TraderService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by homepppp on 2018/5/29.
 */
@Component
public class ExecOrder {
    @Autowired
    OrderService orderService;
    @Autowired
    BlotterService blotterService;
    @Autowired
    TraderService traderService;

    private ConvertToModel convertToModel = new ConvertToModel();
    private ConvertBlotter convertBlotter = new ConvertBlotter();
    private IntOrder intOrder = new IntOrder();

    public static ExecOrder execOrder;

    @PostConstruct
    public void init(){
        execOrder = this;
    }

    public void execMarketOrder(JSONObject obj){
        Order order = convertToModel.convertToMarketOrder(obj);

        traderService = execOrder.traderService;
        orderService = execOrder.orderService;
        blotterService = execOrder.blotterService;

        String company = traderService.getCompanyByUsername(order.getTrader());
        order.setTradeCompany(company);

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
                        return;
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return;
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
                        return;
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return;
                        }
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),nowOrder.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        quantity-=nowOrder.getQuantity();
                        orderService.deleteOrder(nowOrder);
                    }
                }
            }
        }
    }


    public void execStopOrder(JSONObject obj){
        Order order = convertToModel.convertToStopOrder(obj);

        traderService = execOrder.traderService;
        orderService = execOrder.orderService;
        blotterService = execOrder.blotterService;

        String company = traderService.getCompanyByUsername(order.getTrader());
        order.setTradeCompany(company);

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
                    return;
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
                        return;
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return ;
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
                    return;
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
                        return;
                    }
                    else{
                        if(quantity == nowOrder.getQuantity()){
                            orderService.deleteOrder(nowOrder);
                            return;
                        }
                        Blotter blotter = convertBlotter.getBlotter(nowOrder.getProduct(),nowOrder.getPeriod(),nowOrder.getBroker(),nowOrder.getPrice(),nowOrder.getQuantity(),nowOrder.getTrader(),nowOrder.getTradeCompany(),nowOrder.getSide(),order.getTrader(),order.getTradeCompany(),order.getSide());
                        blotterService.saveBlotter(blotter);
                        quantity-=nowOrder.getQuantity();
                        orderService.deleteOrder(nowOrder);
                    }
                }
            }
        }
    }

    public void execLimitOrder(JSONObject obj){

        Order order = convertToModel.convertToLimitOrder(obj);

        orderService = execOrder.orderService;
        traderService = execOrder.traderService;
        blotterService = execOrder.blotterService;

        String company = traderService.getCompanyByUsername(order.getTrader());
        order.setTradeCompany(company);

        List<Order> list = orderService.getDepth(order.getProduct(),order.getPeriod());

        System.out.print(list.size()+"----------------------------------------");

        if(list.size()==0){
            orderService.saveLimitOrder(order);
            return;
        }

        List<List<Order>> sepList = intOrder.getIntOrder(list);

        int i =0;
        for(;i<sepList.size();i++){
            if(sepList.get(i).get(0).getSide()==1)
                break;
        }
        if(order.getSide() == 0){
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
                            return;
                        }
                        else{
                            int nowQuantity = order.getQuantity()-num;
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
                    return;
                }
            }
            orderService.saveLimitOrder(order);
        }
        else if(order.getSide() == 1){
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
                            return;
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
                    return;
                }
            }
            orderService.saveLimitOrder(order);
        }
    }
}
