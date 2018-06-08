package com.tradergateway;

import javax.jms.Destination;

import com.tradergateway.service.ProducerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TraderApplicationTests {

    @Autowired
    ProducerService producerService;

    @Test
    public void contextLoads() throws InterruptedException {
    }

}
