package org.example.server.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tripId", nullable = false)
    private int tripId;
    @Column(name = "modId", nullable = false)
    private int modId;
    @Column(name = "routeId", nullable = false)
    private int routeId;
    @Column(name = "driverId", nullable = false)
    private int driverId;
    @Column(name = "vehicleId", nullable = false)
    private int vehicleId;
    @Column(name = "departureTime", nullable = false)
    private String departureTime;
    @Column(name = "departureDate", nullable = false)
    private String departureDate;
    @Column(name = "arrivalTime", nullable = false)
    private String arrivalTime;
    @Column(name = "arrivalDate", nullable = false)
    private String arrivalDate;
    @Column(name = "availableSeats", nullable = false)
    private int availableSeats;
    @Column(name = "ticketPrice", nullable = false)
    private int ticketPrice;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name ="nameVehicle", nullable = false)
    private String nameVehicle;

    public Trip() {
    }

    public Trip(int tripId, int modId, int routeId, int driverId, int vehicleId, String departureTime, String departureDate, String arrivalTime, String arrivalDate, int availableSeats, int ticketPrice, String status, String nameVehicle) {
        this.tripId = tripId;
        this.modId = modId;
        this.routeId = routeId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.arrivalTime = arrivalTime;
        this.arrivalDate = arrivalDate;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
        this.status = status;
        this.nameVehicle = nameVehicle;
    }

    public String getNameVehicle() {
        return nameVehicle;
    }

    public void setNameVehicle(String nameVehicle) {
        this.nameVehicle = nameVehicle;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
