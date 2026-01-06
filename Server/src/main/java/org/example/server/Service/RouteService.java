package org.example.server.Service;

import jakarta.transaction.Transactional;
import org.example.server.Model.Layout;
import org.example.server.Model.Repository.LocationRepository;
import org.example.server.Model.Repository.RouteRepository;
import org.example.server.Model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private LocationRepository locationRepository;
    public RouteService() {
    }

    public Route addRoute(Route route) {
        return routeRepository.save(route);
    }

    public void deleteRoute() {
    }

    public void updateRoute() {
    }

    public Page<Route> getAllRoutesByModId(int modId, Pageable pageable) {
        return routeRepository.findByModId(modId, pageable);
    }
    public Route getRouteByRouteId(int routeId){
        return routeRepository.findByRouteId(routeId);
    }
    @Transactional
    public void deleteRouteByRouteId(int routeId){
        locationRepository.deleteByRouteId(routeId);
        routeRepository.deleteByRouteId(routeId);
    }
}
