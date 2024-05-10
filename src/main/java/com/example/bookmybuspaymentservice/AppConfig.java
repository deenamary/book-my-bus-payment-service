package com.example.bookmybuspaymentservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${kafka.payment.topic.name}")
    String topicname;

    @Bean("payment_kafka_topic")
    String getBookingKafkaTopic() {
        return topicname;
    }

}
