package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        Spot spot=reservation.getSpot();
        int reqAmount=spot.getPricePerHour()*reservation.getNumberOfHours();
        String paymentmode=mode.toUpperCase();
        Payment payment=new Payment();
        payment.setPaymentCompleted(false);
        payment.setReservation(reservation);
        if(paymentmode.equals("CARD"))
            payment.setPaymentMode(PaymentMode.CARD);
        else if(paymentmode.equals("CASH"))
            payment.setPaymentMode(PaymentMode.CASH);
        else if(paymentmode.equals("UPI"))
            payment.setPaymentMode(PaymentMode.UPI);
        else
            throw new Exception("Payment mode not detected");
        if(reqAmount>amountSent)
            throw new Exception("Insufficient Amount");
        payment.setPaymentCompleted(true);
        //make spot not occupied again
        spot.setOccupied(false);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);
        return payment;
    }
}
