package io.github.eelizan1.parkinglot.services;

import io.github.eelizan1.parkinglot.interfaces.PricingStrategy;
import io.github.eelizan1.parkinglot.model.Ticket;

public class DynamicPricing implements PricingStrategy {
    // Complex logic: surge pricing, time of day, etc.
    @Override
    public Double calculateFee(Ticket ticket, Long exitTime) {
        return 0.0;
    }
}
