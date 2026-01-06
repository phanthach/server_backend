package org.example.server.Model.Response;

import org.example.server.Model.Trip;
import org.example.server.Model.Vehicle;

public class TripVehicleResponse {
    private Trip trip;
    private Vehicle vehicle;

    public TripVehicleResponse() {
    }

    public TripVehicleResponse(Trip trip, Vehicle vehicle) {
        this.trip = trip;
        this.vehicle = vehicle;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
