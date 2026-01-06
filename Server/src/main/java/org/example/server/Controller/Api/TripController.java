package org.example.server.Controller.Api;

import lombok.extern.slf4j.Slf4j;
import org.example.server.Model.Response.TripVehicleResponse;
import org.example.server.Model.Trip;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequestMapping("/api")
public class TripController {
    @Autowired
    private TripService tripService;
    private JwtDecodeService jwtDecodeService;
    private JwtEncodeService jwtEncodeService;

    public TripController(JwtDecodeService jwtDecodeService, JwtEncodeService jwtEncodeService) {
        this.jwtDecodeService = jwtDecodeService;
        this.jwtEncodeService = jwtEncodeService;
    }
    @GetMapping("/listtrip")
    public ResponseEntity<?> listTrip(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PageableDefault(size = 10) Pageable pageable) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        }
        else{
            int modId = jwtDecodeService.extractUserId(token);
            Page<Trip> trips = tripService.getTripsByModId(modId, pageable);
            return ResponseEntity.ok(trips);
        }
    }
    @GetMapping("/listtripstatus")
    public ResponseEntity<?> listTripStatus(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestParam int status, @PageableDefault(size = 10) Pageable pageable) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        }
        else{
            int modId = jwtDecodeService.extractUserId(token);
            Page<Trip> trips = tripService.getTripsByModIdStatus(modId, status, pageable);
            return ResponseEntity.ok(trips);
        }
    }
    @PostMapping("/addtrip")
    public ResponseEntity<?> addTrip(@RequestHeader(value="Authorization", required = false) String authorizationHeader, @RequestBody Trip trip) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        }
        else{
            try {
                int modId = jwtDecodeService.extractUserId(token);
                trip.setModId(modId);
                tripService.save(trip);
                return ResponseEntity.ok("Thêm chuyến thành công");
            } catch (Exception e) {
                // Log lỗi để tiện theo dõi
                e.printStackTrace();
                return ResponseEntity.status(500).body("Đã có lỗi xảy ra khi thêm chuyến");
            }
        }
    }
    @GetMapping("/searchtrip")
    public ResponseEntity<?> searchTrip(@RequestHeader(value="Authorization", required = false) String authorizationHeader,@RequestParam String startLocation, @RequestParam String endLocation, @RequestParam String departureDate, @PageableDefault(size = 20) Pageable pageable) {
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        }
        else{
            log.info("startLocation: " + startLocation);
            log.info("endLocation: " + endLocation);
            log.info("departureDate: " + departureDate);
            Page<TripVehicleResponse> trips = tripService.findTripsByLocationAndDate(startLocation, endLocation, departureDate, pageable);
            return ResponseEntity.ok(trips);
        }
    }
}
