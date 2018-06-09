package com.brokergateway.service;


import com.brokergateway.model.broker;
import com.brokergateway.repository.BrokerRepository;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerService {

    @Autowired
    private BrokerRepository brokerRepository;

    public JSONObject getBroker(JSONObject obj){
        String username = (String)obj.get("username");
        String password = (String)obj.get("password");
        broker b = brokerRepository.getBrokerByUsernameAndPassword(username,password);
        if(b==null){
            return null;
        }
        return JSONObject.fromObject(b);
    }

    public JSONObject register(JSONObject obj){
        broker b = new broker();
        b.setPassword((String)obj.get("password"));
        b.setUsername((String)obj.get("username"));
        brokerRepository.saveAndFlush(b);
        return JSONObject.fromObject(b);
    }

}
