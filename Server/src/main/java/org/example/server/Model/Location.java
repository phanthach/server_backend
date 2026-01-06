package org.example.server.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private int locationId;
    @Column(name = "routeId", nullable = false)
    private int routeId;

    @Column(name = "name", nullable = false)
    private String nameLocation;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "locationType", nullable = false)
    private int locationType;

    @Column(name = "arrivalTime", nullable = false)
    private String arrivalTime;

    public Location() {
    }

    public Location(Integer locationId, int routeId, String nameLocation, String latitude, String longitude, int locationType, String arrivalTime) {
        this.locationId = locationId;
        this.routeId = routeId;
        this.nameLocation = nameLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = locationType;
        this.arrivalTime = arrivalTime;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getNameLocation() {
        return nameLocation;
    }

    public void setNameLocation(String nameLocation) {
        this.nameLocation = nameLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
