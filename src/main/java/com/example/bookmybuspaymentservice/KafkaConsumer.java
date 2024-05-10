package com.example.bookmybuspaymentservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class KafkaConsumer {

    private PaymentRepository paymentRepository;
    private KafkaProducer kafkaProducer;

    KafkaConsumer(PaymentRepository paymentRepository,
                  KafkaProducer kafkaProducer) {
        this.paymentRepository = paymentRepository;
        this.kafkaProducer = kafkaProducer;
    }

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "book-my-bus-booking-topic", groupId = "console-consumer-68654")
    public void consumeMessage(String message) throws JsonProcessingException {
        logger.info("Received message: " + message);

        ObjectMapper mapper = new ObjectMapper();
        BookingMessage bookingMessage = mapper.readValue(message,BookingMessage.class);

        Payment payment = new Payment();
        payment.setBookingId(bookingMessage.getBookingId());
        payment.setDateOfPayment(new Date());
        String paymentId = UUID.randomUUID().toString();
        payment.setPaymentId(paymentId);
        paymentRepository.save(payment);
        logger.info("Saved payment details {}",payment);

        //send message to payment topic
        PaymentMessage paymentMessage = new PaymentMessage();
        paymentMessage.setBookingId(bookingMessage.getBookingId());
        paymentMessage.setBusId(bookingMessage.getBusId());
        paymentMessage.setPaymentId(paymentId);
        paymentMessage.setNoOfSeats(bookingMessage.getNoOfSeats());

        try {
            String paymentmessage = mapper.writeValueAsString(paymentMessage);
            kafkaProducer.sendMessage(paymentmessage);
            logger.info("Successfully sent the payment message {}", paymentmessage);
        } catch (JsonProcessingException e) {
            logger.error("Exception occurred while sending message to payment topic {}", e.getMessage());
        }



    }
}