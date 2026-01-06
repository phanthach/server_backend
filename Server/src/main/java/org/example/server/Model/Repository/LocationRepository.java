package org.example.server.Model.Repository;

import org.example.server.Model.Location;
import org.example.server.Model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.routeId =:routeId")
    Page<Location> findByRouteId(@Param("routeId") int routeId, Pageable pageable);

    void deleteLocationByLocationId(int locationId);
    void deleteByRouteId(int routeId);
    Location findByLocationId(int locationId);

    List<Location> findByRouteIdAndLocationType(int routeId, int locationType);

    List<Location> findByRouteId(int routeId);
    @Modifying
    @Query("UPDATE Location l SET l.routeId = :routeId, l.nameLocation = :nameLocation, l.latitude = :latitude, "
            + "l.longitude = :longitude, l.locationType = :locationType, l.arrivalTime = :arrivalTime WHERE l.locationId = :locationId")
    int updateLocation(@Param("routeId") int routeId,
                       @Param("nameLocation") String nameLocation,
                       @Param("latitude") String latitude,
                       @Param("longitude") String longitude,
                       @Param("locationType") int locationType,
                       @Param("arrivalTime") String arrivalTime,
                       @Param("locationId") int locationId);
}
