package com.brokergateway.Controller;

import com.brokergateway.service.BrokerService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrokerController {

    @Autowired
    BrokerService brokerService;

    @RequestMapping(value = "/broker",method = RequestMethod.POST)
    public ResponseEntity<JSONObject> getBroker(@RequestBody JSONObject obj){
        JSONObject b = brokerService.getBroker(obj);
        if(b==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(b,HttpStatus.OK);
    }

    @RequestMapping(value = "/broker/create",method = RequestMethod.POST)
    public ResponseEntity<JSONObject> createBroker(@RequestBody JSONObject obj){
        JSONObject b = brokerService.register(obj);
        return new ResponseEntity<>(b,HttpStatus.OK);
    }

}
