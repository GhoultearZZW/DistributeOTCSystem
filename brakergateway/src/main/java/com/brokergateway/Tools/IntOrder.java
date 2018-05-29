package com.brokergateway.Tools;

import com.brokergateway.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by homepppp on 2018/5/24.
 */
public class IntOrder {
    public List<List<Order>> getIntOrder(List<Order> list){
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
        return sepList;
    }
}