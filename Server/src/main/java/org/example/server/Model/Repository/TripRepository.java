package org.example.server.Model.Repository;

import org.example.server.Model.Layout;
import org.example.server.Model.Response.TripVehicleResponse;
import org.example.server.Model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT t FROM Trip t WHERE t.modId =:modId")
    Page<Trip> findByModId(@Param("modId") int modId, Pageable pageable);

    @Query("SELECT t FROM Trip t WHERE t.modId =:modId AND t.status = :status")
    Page<Trip> findByModIdStatus(@Param("modId") int modId, int status, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Trip t JOIN Vehicle v ON t.vehicleId = v.vehicleId WHERE v.plateNumber = :plateNumber AND t.departureTime = :departureTime")
    int existsByPlateNumberAndDepartureTime(@Param("plateNumber") String plateNumber, @Param("departureTime") String departureTime);

    @Query("SELECT new org.example.server.Model.Response.TripVehicleResponse(t, v) "
            + "FROM Trip t "
            + "JOIN Route r ON t.routeId = r.routeId "
            + "JOIN Vehicle v ON t.vehicleId = v.vehicleId "
            + "WHERE r.startLocation LIKE CONCAT('%', :startLocation, '%') "
            + "AND r.endLocation LIKE CONCAT('%', :endLocation, '%') "
            + "AND t.departureDate LIKE CONCAT('%', :departureDate, '%')")
    Page<TripVehicleResponse> findTripsWithVehicleByLocationAndDate(@Param("startLocation") String startLocation,
                                                                    @Param("endLocation") String endLocation,
                                                                    @Param("departureDate") String departureDate,
                                                                    Pageable pageable);
}
