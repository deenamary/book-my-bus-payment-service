package com.example.bookmybuspaymentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String paymentKafkaTopic;
    Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                         String payment_kafka_topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.paymentKafkaTopic=payment_kafka_topic;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(paymentKafkaTopic, message);
        logger.info("Message {} has been successfully sent to the topic: {}" ,message, paymentKafkaTopic);
    }
}
