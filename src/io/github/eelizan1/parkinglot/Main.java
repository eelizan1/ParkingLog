package io.github.eelizan1.parkinglot;

import io.github.eelizan1.parkinglot.enums.SpotType;
import io.github.eelizan1.parkinglot.enums.VehicleType;
import io.github.eelizan1.parkinglot.model.ParkingSpot;
import io.github.eelizan1.parkinglot.model.Ticket;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        ParkingSpot parkingSpotCar = new ParkingSpot(
                UUID.randomUUID().toString(),
                SpotType.CAR
        );
        ParkingSpot parkingSpotMotorcycle = new ParkingSpot(
                UUID.randomUUID().toString(),
                SpotType.MOTORCYCLE
        );
        ParkingSpot parkingSpotLarge = new ParkingSpot(
                UUID.randomUUID().toString(),
                SpotType.LARGE
        );
        List<ParkingSpot> parkingSpotList = Arrays.asList(
                parkingSpotCar,
                parkingSpotLarge,
                parkingSpotMotorcycle
        );

        // create parking lot
        ParkingLot parkingLot = new ParkingLot(parkingSpotList, 5.0);

        // car enters
        Ticket carTicket = parkingLot.enter(VehicleType.CAR);

        // car exits
        Double carFee = parkingLot.exit(carTicket.getId());
    }
}