package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot(name,address);
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        SpotType spotType;
        if(numberOfWheels<=2) spotType=SpotType.TWO_WHEELER;
        else if(numberOfWheels<=4) spotType=SpotType.FOUR_WHEELER;
        else spotType=SpotType.OTHERS;
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=new Spot(spotType,pricePerHour,parkingLot);
        return spotRepository1.save(spot);
    }

    @Override
    public void deleteSpot(int spotId) {
      spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
     Spot spot=spotRepository1.findById(spotId).get();
     ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
     spot.setParkingLot(parkingLot);
     spot.setPricePerHour(pricePerHour);
     return spotRepository1.save(spot);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
       ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
       for(Spot spot:parkingLot.getSpotList())
       {
           deleteSpot(spot.getId());
       }
       parkingLotRepository1.delete(parkingLot);
    }
}
