package org.example.server.Service;

import org.example.server.Model.Repository.TripRepository;
import org.example.server.Model.Response.TripVehicleResponse;
import org.example.server.Model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public Page<Trip> getTripsByModId(int modId, Pageable pageable) {
        return tripRepository.findByModId(modId, pageable);
    }
    public Page<Trip> getTripsByModIdStatus(int modId, int status, Pageable pageable) {
        return tripRepository.findByModIdStatus(modId, status, pageable);
    }
    public int existsByPlateNumberAndDepartureTime(String plateNumber, String departureTime) {
        return tripRepository.existsByPlateNumberAndDepartureTime(plateNumber, departureTime);
    }
    public void save(Trip trip) {
        tripRepository.save(trip);
    }
    public Page<TripVehicleResponse> findTripsByLocationAndDate(String startLocation, String endLocation, String departureDate, Pageable pageable) {
        return tripRepository.findTripsWithVehicleByLocationAndDate(startLocation, endLocation, departureDate, pageable);
    }
}
