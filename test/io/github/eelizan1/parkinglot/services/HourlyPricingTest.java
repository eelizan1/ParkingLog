package io.github.eelizan1.parkinglot.services;

import io.github.eelizan1.parkinglot.enums.VehicleType;
import io.github.eelizan1.parkinglot.model.Ticket;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class HourlyPricingTest {
    private static final long ENTRY_TIME = 1_000_000L;

    private final HourlyPricing pricing = new HourlyPricing(5.0);

    @Test
    void zeroDurationIsFree() {
        assertEquals(0.0, feeAfter(Duration.ZERO));
    }

    @Test
    void oneMillisecondIsChargedAsOneHour() {
        assertEquals(5.0, feeAfter(Duration.ofMillis(1)));
    }

    @Test
    void exactlyOneHour() {
        assertEquals(5.0, feeAfter(Duration.ofHours(1)));
    }

    @Test
    void exactlyThreeHours() {
        assertEquals(15.0, feeAfter(Duration.ofHours(3)));
    }

    @Test
    void partialHourRoundsUp() {
        assertEquals(20.0, feeAfter(Duration.ofHours(3).plusMillis(1)));
    }

    @Test
    void justUnderAnHourRoundsUp() {
        assertEquals(10.0, feeAfter(Duration.ofHours(2).minusMillis(1)));
    }

    @Test
    void multiDayDuration() {
        assertEquals(360.0, feeAfter(Duration.ofDays(3)));
    }

    @Test
    void usesConfiguredRate() {
        HourlyPricing pricing = new HourlyPricing(2.5);

        assertEquals(7.5, pricing.calculateFee(ticketAt(ENTRY_TIME), ENTRY_TIME + Duration.ofHours(3).toMillis()));
    }

    @Test
    void zeroRateIsAlwaysFree() {
        HourlyPricing pricing = new HourlyPricing(0.0);

        assertEquals(0.0, pricing.calculateFee(ticketAt(ENTRY_TIME), ENTRY_TIME + Duration.ofHours(10).toMillis()));
    }

    @Test
    void feeDependsOnDurationNotVehicleType() {
        long exitTime = ENTRY_TIME + Duration.ofHours(2).toMillis();

        for (VehicleType type : VehicleType.values()) {
            Ticket ticket = new Ticket("t", "s", type, ENTRY_TIME);
            assertEquals(10.0, pricing.calculateFee(ticket, exitTime), type.name());
        }
    }

    private Double feeAfter(Duration duration) {
        return pricing.calculateFee(ticketAt(ENTRY_TIME), ENTRY_TIME + duration.toMillis());
    }

    private static Ticket ticketAt(long entryTime) {
        return new Ticket("ticket-1", "spot-1", VehicleType.CAR, entryTime);
    }
}
