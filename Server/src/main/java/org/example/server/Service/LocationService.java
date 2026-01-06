package org.example.server.Service;

import jakarta.transaction.Transactional;
import org.example.server.Model.Location;
import org.example.server.Model.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Page<Location> findByRouteId(int routeId, Pageable pageable){
        return locationRepository.findByRouteId(routeId, pageable);
    }
    public void addLocation(Location location) {
        locationRepository.save(location);
    }
    @Transactional
    public void deleteLocationByLocationId(int locationId){
        locationRepository.deleteLocationByLocationId(locationId);
    }
    public Location getLocationByLocationId(int locationId){
        return locationRepository.findByLocationId(locationId);
    }
    public List<Location> getLocationsByRouteIdAndLocationType(int routeId, int locationType) {
        return locationRepository.findByRouteIdAndLocationType(routeId, locationType);
    }
    public List<Location> getLocationsByRouteId(int routeId) {
        return locationRepository.findByRouteId(routeId);
    }
    @Transactional
    public int updateLocation(int routeId, String nameLocation, String latitude, String longitude, int locationType, String arrivalTime, int locationId){
        return locationRepository.updateLocation(routeId, nameLocation, latitude, longitude, locationType, arrivalTime, locationId);
    }
}
