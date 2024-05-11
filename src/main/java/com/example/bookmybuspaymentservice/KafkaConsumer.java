package com.example.bookmybuspaymentservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
    private final String paymentKafkaTopic;
    private final String paymentFailureKafkaTopic;

    KafkaConsumer(PaymentRepository paymentRepository,
                  KafkaProducer kafkaProducer,
                  String payment_kafka_topic,
                  String payment_failure_kafka_topic) {
        this.paymentRepository = paymentRepository;
        this.kafkaProducer = kafkaProducer;
        this.paymentKafkaTopic=payment_kafka_topic;
        this.paymentFailureKafkaTopic = payment_failure_kafka_topic;
    }

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "book-my-bus-booking-topic", groupId = "console-consumer-68654")
    public void consumeMessage(String message) throws JsonProcessingException {
        logger.info("Received message: " + message);

        ObjectMapper mapper = new ObjectMapper();
        BookingMessage bookingMessage = mapper.readValue(message, BookingMessage.class);
        try {
            Payment payment = new Payment();
            payment.setBookingId(bookingMessage.getBookingId());
            payment.setDateOfPayment(new Date());
            String paymentId = UUID.randomUUID().toString();
            payment.setPaymentId(paymentId);
            paymentRepository.save(payment);
            logger.info("Saved payment details {}", payment);

            //send message to payment topic
            PaymentMessage paymentMessage = new PaymentMessage();
            paymentMessage.setBookingId(bookingMessage.getBookingId());
            paymentMessage.setBusId(bookingMessage.getBusId());
            paymentMessage.setPaymentId(paymentId);
            paymentMessage.setNoOfSeats(bookingMessage.getNoOfSeats());

            try {
                String paymentmessage = mapper.writeValueAsString(paymentMessage);
                kafkaProducer.sendMessage(paymentKafkaTopic, paymentmessage);
                logger.info("Successfully sent the payment message {}", paymentmessage);
            } catch (JsonProcessingException e) {
                logger.error("Exception occurred while sending message to payment topic {}", e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            //IMPLEMENTING EXCEPTION HANDLING USING CHOREOGRAPHY SAGA PATTERN
            logger.error("Exception occurred while processing payment{}. Exception details : {}",message, e.getMessage());
            kafkaProducer.sendMessage(paymentFailureKafkaTopic, bookingMessage.getBookingId());
        }
    }

    @Transactional
    @KafkaListener(topics = "book-my-bus-inventory-update-failure-topic", groupId = "console-consumer-68654")
    public void consumeInventoryUpdateFailureMessage(String message) throws JsonProcessingException {
        logger.info("Received message: " + message);
        String bookingId = message;
        paymentRepository.deleteAllByBookingId(bookingId);
        logger.info("Deleted payment");
        kafkaProducer.sendMessage(paymentFailureKafkaTopic,bookingId);
    }
}