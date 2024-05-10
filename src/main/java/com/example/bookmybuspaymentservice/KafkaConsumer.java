package com.example.bookmybuspaymentservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "book-my-bus-booking-topic", groupId = "console-consumer-68654")
    public void consumeMessage(String message) throws JsonProcessingException {
        logger.info("Received message: " + message);

        ObjectMapper mapper = new ObjectMapper();
        BookingMessage bookingMessage = mapper.readValue(message,BookingMessage.class);
    }
}