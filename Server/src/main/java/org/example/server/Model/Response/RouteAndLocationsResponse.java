package org.example.server.Model.Response;

import org.example.server.Model.Location;
import org.example.server.Model.Route;

import java.util.List;

// Định nghĩa DTO
public class RouteAndLocationsResponse {
    private Route route;
    private List<Location> locations;

    // Constructor, getter và setter
    public RouteAndLocationsResponse(Route route, List<Location> locations) {
        this.route = route;
        this.locations = locations;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

