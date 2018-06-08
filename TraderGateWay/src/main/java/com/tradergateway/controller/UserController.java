package com.tradergateway.controller;

import com.tradergateway.Tools.JsonToProtocol;
import com.tradergateway.model.Trader;
import com.tradergateway.service.ProducerService;
import com.tradergateway.service.TraderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import javax.jms.Destination;

/**
 * Created by homepppp on 2018/5/22.
 */
@RestController
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    TraderService traderService;
    @Autowired
    ProducerService producerService;

    private JsonToProtocol jsonToProtocol = new JsonToProtocol();

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> Register(@RequestBody JSONObject obj) {
        JSONObject newObj = traderService.createUser(obj);
        return new ResponseEntity<JSONObject>(newObj, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> Login(@RequestBody JSONObject obj) {
        JSONObject newObj = traderService.getUserByUsernameAndPwd(obj);
        if (newObj == null) {
            return new ResponseEntity<JSONObject>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<JSONObject>(newObj, HttpStatus.OK);
    }

}
