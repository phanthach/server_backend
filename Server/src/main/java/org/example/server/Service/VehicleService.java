package org.example.server.Service;

import org.example.server.Model.Repository.VehicleRepository;
import org.example.server.Model.User;
import org.example.server.Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    public Page<Vehicle> getVehicleByModId(int modId, Pageable pageable) {
        return vehicleRepository.findByModId(modId, pageable);
    }

    public void updateStatus(int vehicleId, int status) {
        vehicleRepository.updateStatus(vehicleId, status);
    }

    public Vehicle getVehecleByVehicleId(int vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByVehicleId(vehicleId);
        return vehicleOptional.orElse(null);
    }
}
