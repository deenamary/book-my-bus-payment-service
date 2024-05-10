package com.example.bookmybuspaymentservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @Column(name = "paymentid", length = 255)
    private String paymentId;

    @Column(name = "bookingid", length = 255)
    private String bookingId;

    @Column(name = "date_of_payment", length = 20)
    private Date dateOfPayment;


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Date dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", dateOfPayment=" + dateOfPayment +
                '}';
    }
}
