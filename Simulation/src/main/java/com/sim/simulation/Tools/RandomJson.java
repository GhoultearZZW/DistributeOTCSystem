package com.sim.simulation.Tools;

import net.sf.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class RandomJson {

    private static ArrayList<String> periodList = new ArrayList<String>(){
        {
            add("SEP16");
            add("MAR16");
        }
    };

    private static ArrayList<String> productList = new ArrayList<String>(){
        {
            add("gold");
            add("silver");
            add("oil");
            add("soybean");
        }
    };

    public JSONObject getLimitOrder(){
        JSONObject obj = new JSONObject();

        Random random = new Random(System.currentTimeMillis());

        obj.put("broker","M");
        obj.put("orderType","LimitOrder");
        obj.put("period",periodList.get(random.nextInt(periodList.size())));
        obj.put("product",productList.get(random.nextInt(productList.size())));
        obj.put("quantity",random.nextInt(100));
        obj.put("trader","jolin");
        obj.put("side",random.nextInt(2));
        obj.put("price",random.nextInt(100));

        return obj;
    }

    public JSONObject getMarketOrder(){
        JSONObject obj = new JSONObject();

        Random random = new Random(System.currentTimeMillis());
        obj.put("broker","M");
        obj.put("orderType","MarketOrder");
        obj.put("period",periodList.get(random.nextInt(periodList.size())));
        obj.put("product",productList.get(random.nextInt(productList.size())));
        obj.put("quantity",random.nextInt(100));
        obj.put("trader","jolin");
        obj.put("side",random.nextInt(2));

        return obj;
    }

    public JSONObject getStopOrder(){
        JSONObject obj = new JSONObject();

        Random random = new Random(System.currentTimeMillis());
        obj.put("broker","M");
        obj.put("orderType","StopOrder");
        obj.put("period",periodList.get(random.nextInt(periodList.size())));
        obj.put("product",productList.get(random.nextInt(productList.size())));
        obj.put("quantity",random.nextInt(100));
        obj.put("trader","jolin");
        obj.put("side",random.nextInt(2));
        obj.put("price",random.nextInt(100));

        return obj;
    }

    public JSONObject getCancelOrder(){
        JSONObject obj = new JSONObject();

        return obj;
    }

}
