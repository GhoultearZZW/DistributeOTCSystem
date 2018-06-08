package com.tradergateway.controller;

import com.tradergateway.model.Blotter;
import com.tradergateway.service.BlotterService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by homepppp on 2018/5/28.
 */
@RestController
public class BlotterController {
    @Autowired
    BlotterService blotterService;

    @RequestMapping(value = "blotter", method = RequestMethod.POST)
    public ResponseEntity<List<Blotter>> getBlotter(@RequestBody JSONObject obj) {
        String product = (String) obj.get("product");
        String period = (String) obj.get("period");
        String broker = (String) obj.get("broker");

        List<Blotter> list = blotterService.getBlotters(product, period, broker);
        return new ResponseEntity<List<Blotter>>(list, HttpStatus.OK);
    }
}
