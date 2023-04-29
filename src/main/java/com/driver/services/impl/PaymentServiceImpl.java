package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
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
        String paymentMode=mode.toUpperCase();
        Payment payment;
        if(paymentMode.equals("CASH")||paymentMode.equals("CARD")||paymentMode.equals("UPI"))
        {
            Reservation currentReservation=reservationRepository2.findById(reservationId).get();
            int reqAmount=currentReservation.getNumberOfHours()*currentReservation.getSpot().getPricePerHour();
            if(reqAmount>amountSent)
                throw new Exception("Insufficient Amount");
            //prepare payment
            PaymentMode currentPaymentMode;
            if(paymentMode=="CASH") currentPaymentMode=PaymentMode.CASH;
            if(paymentMode=="CARD") currentPaymentMode=PaymentMode.CARD;
            else currentPaymentMode=PaymentMode.UPI;
            payment=new Payment(true,currentPaymentMode,currentReservation);
            //before saving make spot occupied and add payment to reservation
            currentReservation.getSpot().setOccupied(true);
            currentReservation.setPayment(payment);
            //save
            paymentRepository2.save(payment);
        }
        else {
            throw new Exception("Payment mode not detected");
        }
        return payment;
    }
}
