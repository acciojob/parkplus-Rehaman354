package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.
        Reservation newReservation=new Reservation();
        newReservation.setNumberOfHours(timeInHours);
        ParkingLot parkingLot;
        try {
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e) {
            throw new Exception("Cannot make reservation");
        }
        User user;
        try{
            user=userRepository3.findById(userId).get();
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        newReservation.setUser(user);
        List<Spot> spots=parkingLot.getSpotList();
        Spot reservedSpot=null;
        int optimalPrice = Integer.MAX_VALUE;
        for(Spot spot: spots){
            if(!spot.getOccupied()){
                if(spot.getSpotType().equals(SpotType.TWO_WHEELER)){
                    if(numberOfWheels <= 2){
                        if(optimalPrice > spot.getPricePerHour()){
                            optimalPrice = spot.getPricePerHour();
                            reservedSpot = spot;
                        }
                    }
                } else if(spot.getSpotType().equals(SpotType.FOUR_WHEELER)){
                    if(numberOfWheels <= 4){
                        if(optimalPrice > spot.getPricePerHour()){
                            optimalPrice = spot.getPricePerHour();
                            reservedSpot= spot;
                        }
                    }
                } else{
                    if(optimalPrice > spot.getPricePerHour()){
                        optimalPrice = spot.getPricePerHour();
                        reservedSpot = spot;
                    }
                }
            }
        }
        if(reservedSpot==null)
            throw new Exception("Cannot make reservation");
        newReservation.setSpot(reservedSpot);
        user.getReservationList().add(newReservation);
        reservedSpot.setOccupied(true);
        reservedSpot.getReservationList().add(newReservation);
        spotRepository3.save(reservedSpot);
       return  reservationRepository3.save(newReservation);
    }
}
