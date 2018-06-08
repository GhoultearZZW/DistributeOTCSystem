package com.brokergateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrakergatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrakergatewayApplication.class, args);
    }
}
