package org.example.server.Controller.Api;

import lombok.extern.slf4j.Slf4j;
import org.example.server.Model.Location;
import org.example.server.Model.Response.LocationResponse;
import org.example.server.Model.Response.LoginResponse;
import org.example.server.Model.Response.RouteAndLocationsResponse;
import org.example.server.Model.Route;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.LocationService;
import org.example.server.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private RouteService routeService;
    private JwtDecodeService jwtDecodeService;
    private JwtEncodeService jwtEncodeService;

    public LocationController(JwtDecodeService jwtDecodeService, JwtEncodeService jwtEncodeService) {
        this.jwtDecodeService = jwtDecodeService;
        this.jwtEncodeService = jwtEncodeService;
    }

    @PostMapping("/addlocation")
    public ResponseEntity<?> addLocation(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Location location) {
        log.info(location.toString());
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Route route = routeService.getRouteByRouteId(location.getRouteId());
            if (route == null || route.getModId() != modId) {
                return ResponseEntity.status(403).body("Bạn không có quyền chỉnh sửa tuyến đường này");
            } else {
                if (location.getLocationType() == 0 || location.getNameLocation().isEmpty() || location.getLatitude().isEmpty() || location.getLongitude().isEmpty() || location.getArrivalTime().isEmpty()) {
                    return ResponseEntity.status(400).body("Thiếu thông tin vị trí, vui lòng bổ sung thêm thông tin");
                } else {
                    locationService.addLocation(location);
                    return ResponseEntity.ok(location);
                }
            }
        }
    }

    @DeleteMapping("/deletelocation")
    public ResponseEntity<?> deleteLocation(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestParam int locationId) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Location location = locationService.getLocationByLocationId(locationId);
            Route route = routeService.getRouteByRouteId(location.getRouteId());
            if (location == null || route.getModId() != modId) {
                return ResponseEntity.status(403).body("Bạn không có quyền chỉnh sửa vị trí này");
            } else {
                locationService.deleteLocationByLocationId(locationId);
                return ResponseEntity.ok("Xóa vị trí thành công");
            }
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getAllLocationsByRouteId(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestParam int routeId, @RequestParam int locationType) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Route route = routeService.getRouteByRouteId(routeId);
            if (route == null || route.getModId() != modId) {
                return ResponseEntity.status(403).body("Bạn không có quyền xem vị trí của tuyến đường này");
            } else {
                List<Location> locations = locationService.getLocationsByRouteIdAndLocationType(routeId, locationType);
                return ResponseEntity.ok(locations);
            }
        }
    }
    @PutMapping("/updatelocation")
    public ResponseEntity<?> updateLocation(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Location location) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        log.info(location.toString());
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Location location1 = locationService.getLocationByLocationId(location.getLocationId());
            if(location1 == null){
                return ResponseEntity.status(400).body("Vị trí không tồn tại");
            }
            Route route = routeService.getRouteByRouteId(location1.getRouteId());
            if (location1 == null || route.getModId() != modId) {
                return ResponseEntity.status(403).body("Bạn không có quyền chỉnh sửa vị trí này");
            } else {
                int locationUpdate = locationService.updateLocation(location.getRouteId(), location.getNameLocation(), location.getLatitude(), location.getLongitude(), location.getLocationType(), location.getArrivalTime(), location.getLocationId());
                if(locationUpdate == 0){
                    return ResponseEntity.status(400).body("Cập nhật vị trí thất bại");
                }
                else {
                    return ResponseEntity.ok("Cập nhật vị trí thành công");
                }
            }
        }
    }
    @GetMapping("/listlocations")
    public ResponseEntity<?> getAllLocationByRouteId(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestParam int routeId) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body(new LocationResponse(403, "Token không hợp lệ", null));
        }
        String token = authorizationHeader.substring(7);
        try{
            if (!jwtDecodeService.isTokenValid(token)) {
                return ResponseEntity.status(403).body(new LocationResponse(403, "Token không hợp lệ", null));
            } else {
                    List<Location> locations = locationService.getLocationsByRouteId(routeId);
                    log.info(locations.toString());
                    return ResponseEntity.ok(new LocationResponse(200, "Lấy danh sách vị trí thành công", locations));
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(403).body("Đã có lỗi xảy ra");
        }
    }
}
