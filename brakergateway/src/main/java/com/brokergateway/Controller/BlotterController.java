package com.brokergateway.Controller;

import com.brokergateway.model.Blotter;
import com.brokergateway.model.Trader;
import com.brokergateway.model.TraderOrder;
import com.brokergateway.service.BlotterService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlotterController {

    @Autowired
    BlotterService blotterService;

    @RequestMapping(value = "/blotter/all",method = RequestMethod.POST)
    public ResponseEntity<List<Blotter>> getAllBlotter(@RequestBody JSONObject obj){
        String broker = (String) obj.get("broker");
        List<Blotter> list = blotterService.getAllBlotter(broker);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/blotter",method = RequestMethod.POST)
    public ResponseEntity<List<Blotter>> getRecentBlotter(@RequestBody JSONObject obj){
        String company = (String)obj.get("company");
        String broker = (String)obj.get("broker");
        List<Blotter> list = blotterService.getBlottersByCompany(company,broker);
        return new ResponseEntity<List<Blotter>>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/blotter/initiator",method = RequestMethod.POST)
    public ResponseEntity<List<Blotter>> getBlotterAsInitiator(@RequestBody JSONObject obj){
        String company = (String)obj.get("company");
        String broker =(String)obj.get("broker");
        List<Blotter> list = blotterService.getBlotterByCompanyAsInitiator(company,broker);
        return new ResponseEntity<List<Blotter>>(list,HttpStatus.OK);
    }

    @RequestMapping(value = "/blotter/completion",method = RequestMethod.POST)
    public ResponseEntity<List<Blotter>> getBlotterAsCompletion(@RequestBody JSONObject obj){
        String company = (String)obj.get("company");
        String broker = (String)obj.get("broker");
        List<Blotter> list = blotterService.getBlotterByCompanyAsCompletion(company,broker);
        return new ResponseEntity<List<Blotter>>(list,HttpStatus.OK);
    }
}
