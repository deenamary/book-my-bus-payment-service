package com.example.bookmybuspaymentservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    int deleteAllByBookingId(String bookingId);
}