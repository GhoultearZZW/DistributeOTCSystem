package com.brokergateway.Tools;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProtocolToJson {
    public JSONObject convertToJson(String str) {
        List<String> list = new ArrayList<>();
        int pre = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '-') {
                if (pre == i) {
                    list.add("");
                    pre++;
                    continue;
                }
                list.add(str.substring(pre, i));
                pre = i + 1;
            }
        }
        if(list.size()==2){
            JSONObject obj = new JSONObject();
            obj.put("orderId",list.get(1));
            obj.put("orderType","CancelOrder");
            return obj;
        }
        JSONObject obj = new JSONObject();
        obj.put("broker", list.get(0));
        obj.put("orderType", list.get(1));
        obj.put("period", list.get(2));
        obj.put("product", list.get(3));
        obj.put("quantity", Integer.parseInt(list.get(4)));
        if (!list.get(5).equals("")) {
            obj.put("price", Double.parseDouble(list.get(5)));
        }
        obj.put("side", list.get(6));
        obj.put("trader", list.get(7));
        if (!list.get(8).equals("")) {
            obj.put("method", list.get(8));
        }
        return obj;
    }
}
