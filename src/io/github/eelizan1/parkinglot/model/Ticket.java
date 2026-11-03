package io.github.eelizan1.parkinglot.model;

import io.github.eelizan1.parkinglot.enums.VehicleType;

public class Ticket {
    private String id;
    private String spotId;
    private VehicleType vehicleType;
    private Long entryTime;

    public Ticket(String id, String spotId, VehicleType vehicleType, Long entryTime) {
        this.id = id;
        this.spotId = spotId;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }
}
