package com.example.bookmybuspaymentservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${kafka.payment.topic.name}")
    String topicname;

    @Value("${kafka.payment.failure.topic.name}")
    String paymentFailureTopicName;

    @Bean("payment_kafka_topic")
    String getBookingKafkaTopic() {
        return topicname;
    }

    @Bean("payment_failure_kafka_topic")
    String getPaymentFailureKafkaTopic() {
        return paymentFailureTopicName;
    }

}
