package com.brokergateway.WebSocket;

import com.brokergateway.service.OrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SendInfo {

    @Autowired
    OrderService orderService;
    @Autowired
    private webSocket webSocket1;

    public static SendInfo sendInfo;

    @PostConstruct
    public void init() {
        sendInfo = this;
    }

    private static Map<ArrayList<String>, List<Map<String, Object>>> depthList = new HashMap<>();

    @Scheduled(fixedDelay = 5000)
    public void send() throws Exception {

        orderService = sendInfo.orderService;

        JSONArray arr = new JSONArray();

        List<Map<String, Object>> list = orderService.getAllProducts();
        ArrayList<ArrayList<String>> products = new ArrayList<>();
        //获取所有产品[period,product,broker]
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            ArrayList now = new ArrayList();
            now.add((String) map.get("0"));
            now.add((String) map.get("1"));
            now.add((String) map.get("2"));
            products.add(now);
        }
        //获取所有产品的depth
        for (int i = 0; i < products.size(); i++) {
            ArrayList<String> product = products.get(i);
            List<Map<String, Object>> map = orderService.getDepthSum(product.get(1), product.get(0), product.get(2));
            List<Map<String, Object>> oldMap = depthList.get(product);
            ArrayList<Double> changedPrice = new ArrayList<>();
            for (int j = 0; j < map.size(); j++) {
                if (oldMap == null || !oldMap.contains(map.get(j))) {
                    JSONObject obj = new JSONObject();
                    obj.put("product", product.get(1));
                    obj.put("period", product.get(0));
                    obj.put("broker", product.get(2));
                    obj.put("side", map.get(j).get("0"));
                    obj.put("price", map.get(j).get("1"));
                    obj.put("quantity", map.get(j).get("2"));
                    arr.add(obj);
                    changedPrice.add((Double)map.get(j).get("1"));
                }
            }
            if(oldMap!=null) {
                for (int j = 0; j < oldMap.size(); j++) {
                    if (map == null || !map.contains(oldMap.get(j))) {
                        if(changedPrice.contains((Double)oldMap.get(j).get("1"))){
                            break;
                        }
                        JSONObject obj = new JSONObject();
                        obj.put("product", product.get(1));
                        obj.put("period", product.get(0));
                        obj.put("broker", product.get(2));
                        obj.put("price", oldMap.get(j).get("1"));
                        obj.put("status", 0);
                        arr.add(obj);
                    }
                }
            }
            depthList.remove(product);
            depthList.put(product, map);
        }
        if(arr.size()!=0)
            webSocket1.sendMessage(arr);

        //Thread.sleep(3000);
    }

}
