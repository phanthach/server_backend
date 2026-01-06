package org.example.server.Model.Repository;

import org.example.server.Model.Layout;
import org.example.server.Model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.RouteMatcher;

public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("SELECT r FROM Route r WHERE r.modId =:modId")
    Page<Route> findByModId(@Param("modId") int modId, Pageable pageable);
    Route findByRouteId(int routeId);

    void deleteByRouteId(int routeId);
}
