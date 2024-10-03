package org.example.server.Model.Repository;

import org.example.server.Model.User;
import org.example.server.Model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleId(int vehicleId);
    @Query("SELECT u FROM Vehicle u WHERE u.modId = :modId")
    Page<Vehicle> findByModId(@Param("modId") int modId, Pageable pageable);

    @Modifying
    @Query("UPDATE Vehicle v SET v.status =:status WHERE v.vehicleId =:vehicleId")
    void updateStatus(@Param("vehicleId") int vehicleId, @Param("status") int status);
}