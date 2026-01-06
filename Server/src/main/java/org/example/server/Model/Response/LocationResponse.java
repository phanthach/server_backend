package org.example.server.Model.Response;

import org.example.server.Model.Location;

import java.util.List;

public class LocationResponse {
    private int status;
    private String message;
    private List<Location> locations;

    public LocationResponse() {
    }

    public LocationResponse(int status, String message, List<Location> locations) {
        this.status = status;
        this.message = message;
        this.locations = locations;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
