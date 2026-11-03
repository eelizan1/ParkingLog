package io.github.eelizan1.parkinglot;

import io.github.eelizan1.parkinglot.enums.SpotType;
import io.github.eelizan1.parkinglot.enums.VehicleType;
import io.github.eelizan1.parkinglot.model.ParkingSpot;
import io.github.eelizan1.parkinglot.model.Ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {
    private static final double HOURLY_RATE = 5.0;

    private MutableClock clock;
    private ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        clock = new MutableClock(Instant.parse("2026-01-01T08:00:00Z"));
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot("car-1", SpotType.CAR),
                new ParkingSpot("moto-1", SpotType.MOTORCYCLE),
                new ParkingSpot("large-1", SpotType.LARGE)
        );
        parkingLot = new ParkingLot(spots, HOURLY_RATE, clock);
    }

    // enter

    @Test
    void enterAssignsSpotMatchingVehicleType() {
        assertEquals("car-1", parkingLot.enter(VehicleType.CAR).getSpotId());
        assertEquals("moto-1", parkingLot.enter(VehicleType.MOTORCYCLE).getSpotId());
        assertEquals("large-1", parkingLot.enter(VehicleType.LARGE).getSpotId());
    }

    @Test
    void enterPopulatesTicket() {
        Ticket ticket = parkingLot.enter(VehicleType.CAR);

        assertNotNull(ticket.getId());
        assertEquals(VehicleType.CAR, ticket.getVehicleType());
        assertEquals(clock.millis(), ticket.getEntryTime());
    }

    @Test
    void enterThrowsWhenNoSpotOfTypeIsAvailable() {
        parkingLot.enter(VehicleType.CAR);

        RuntimeException e = assertThrows(RuntimeException.class, () -> parkingLot.enter(VehicleType.CAR));
        assertEquals("No available spots for this parking lot", e.getMessage());
    }

    @Test
    void enterThrowsWhenLotHasNoSpots() {
        ParkingLot emptyLot = new ParkingLot(Collections.emptyList(), HOURLY_RATE, clock);

        assertThrows(RuntimeException.class, () -> emptyLot.enter(VehicleType.CAR));
    }

    @Test
    void enterThrowsForNullVehicleType() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> parkingLot.enter(null));
        assertEquals("Vehicle type not found", e.getMessage());
    }

    @Test
    void enterDoesNotUseSpotOfDifferentType() {
        parkingLot.enter(VehicleType.CAR);

        // the large spot is free, but a car should not be placed there
        assertThrows(RuntimeException.class, () -> parkingLot.enter(VehicleType.CAR));
    }

    // exit

    @Test
    void exitFreesSpotForNextVehicle() {
        Ticket first = parkingLot.enter(VehicleType.CAR);
        parkingLot.exit(first.getId());

        Ticket second = parkingLot.enter(VehicleType.CAR);
        assertEquals("car-1", second.getSpotId());
    }

    @Test
    void exitThrowsForUnknownTicket() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> parkingLot.exit("does-not-exist"));
        assertEquals("Ticket not found", e.getMessage());
    }

    @Test
    void exitThrowsWhenTicketIsUsedTwice() {
        Ticket ticket = parkingLot.enter(VehicleType.CAR);
        parkingLot.exit(ticket.getId());

        assertThrows(RuntimeException.class, () -> parkingLot.exit(ticket.getId()));
    }

    // fees

    @Test
    void feeForExactlyThreeHours() {
        assertEquals(15.0, parkAndExitAfter(Duration.ofHours(3)));
    }

    @Test
    void feeRoundsPartialHourUp() {
        assertEquals(20.0, parkAndExitAfter(Duration.ofHours(3).plusMillis(1)));
    }

    @Test
    void feeForLessThanOneHourIsOneHour() {
        assertEquals(5.0, parkAndExitAfter(Duration.ofMinutes(1)));
    }

    @Test
    void feeForImmediateExitIsZero() {
        assertEquals(0.0, parkAndExitAfter(Duration.ZERO));
    }

    @Test
    void feeForFullDay() {
        assertEquals(120.0, parkAndExitAfter(Duration.ofHours(24)));
    }

    private Double parkAndExitAfter(Duration duration) {
        Ticket ticket = parkingLot.enter(VehicleType.CAR);
        clock.advance(duration);
        return parkingLot.exit(ticket.getId());
    }

    // Clock whose time only moves when the test advances it
    private static class MutableClock extends Clock {
        private Instant now;

        MutableClock(Instant start) {
            this.now = start;
        }

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant instant() {
            return now;
        }
    }
}
