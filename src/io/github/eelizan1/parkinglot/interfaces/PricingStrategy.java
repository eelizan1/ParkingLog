package io.github.eelizan1.parkinglot.interfaces;

import io.github.eelizan1.parkinglot.model.Ticket;

public interface PricingStrategy {
    Double calculateFee(Ticket ticket, Long exitTime);
}
