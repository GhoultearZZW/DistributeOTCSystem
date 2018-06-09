package com.sim.simulation.Tools;

import net.sf.json.JSONObject;

public class JsonToProtocol {

    public String convertToString(JSONObject obj) {
        String str = "";
        String broker = (String) obj.get("broker");
        str = str + broker + "-";
        String orderType = (String) obj.get("orderType");
        str = str + orderType + "-";
        String period = (String) obj.get("period");
        str = str + period + "-";
        String product = (String) obj.get("product");
        str = str + product + "-";
        int quantity = obj.getInt("quantity");
        String strQuantity = "" + quantity;
        str = str + strQuantity + "-";
        if (obj.containsKey("price")) {
            double price = obj.getDouble("price");
            String strPrice = "" + price;
            str = str + strPrice + "-";
        } else {
            str = str + "-";
        }
        int side = obj.getInt("side");
        String strSide = "" + side;
        str = str + strSide + "-";
        String trader = (String) obj.get("trader");
        str = str + trader + "-";
        if (obj.containsKey("method")) {
            String method = (String) obj.get("method");
            str = str + method + "-";
        } else {
            str = str + "-";
        }
        return str;
    }

}
