package io.github.eelizan1.parkinglot.services;

import io.github.eelizan1.parkinglot.interfaces.PricingStrategy;
import io.github.eelizan1.parkinglot.model.Ticket;

public class HourlyPricing implements PricingStrategy {
    private final Double hourlyRate;

    public HourlyPricing(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    // Same logic as before
    @Override
    public Double calculateFee(Ticket ticket, Long exitTime) {
        long durationMillis = exitTime - ticket.getEntryTime();
        long durationHours = durationMillis / (1000 * 60 * 60);

        if (durationMillis % (1000 * 60 * 60) > 0) {
            durationHours++;
        }

        return durationHours * this.hourlyRate;
    }
}
