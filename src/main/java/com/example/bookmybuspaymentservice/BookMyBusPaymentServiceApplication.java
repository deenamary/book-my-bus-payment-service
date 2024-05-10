package com.example.bookmybuspaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookMyBusPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyBusPaymentServiceApplication.class, args);
    }

}
