package org.example.server.Controller.Api;

import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.User;
import org.example.server.Model.Vehicle;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VehicleController {
    private static final Logger log = LoggerFactory.getLogger(DriverController.class);
    private final JwtDecodeService jwtDecodeService;
    private final JwtEncodeService jwtEncodeService;
    private final PagedResourcesAssembler<Vehicle> pagedResourcesAssembler;

    @Autowired
    private VehicleService vehicleService;

    public VehicleController(JwtDecodeService jwtDecodeService, JwtEncodeService jwtEncodeService, PagedResourcesAssembler<Vehicle> pagedResourcesAssembler) {
        this.jwtDecodeService = jwtDecodeService;
        this.jwtEncodeService = jwtEncodeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/vehicles")
    public ResponseEntity<?> getListVehicle(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PageableDefault(size = 10) Pageable pageable) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new TokenResponse(0, "Token không hợp lệ", ""));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(401).body(new TokenResponse(0, "Token không hợp lệ", ""));
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Page<Vehicle> vehicles = vehicleService.getVehicleByModId(modId, pageable);
            log.info("Get list vehicle by modId: " + vehicles.getContent());
            return ResponseEntity.ok(pagedResourcesAssembler.toModel(vehicles));
        }
    }
    @PutMapping("/vehicles/{vehicleId}/block")
    public ResponseEntity<?> updateBlockDriver(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable int vehicleId, @RequestParam int status) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", ""));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", ""));
        } else {
            int userId = jwtDecodeService.extractUserId(token);
            Vehicle vehicle = vehicleService.getVehecleByVehicleId(vehicleId);
            if (userId==vehicle.getModId()) {
                vehicleService.updateStatus(vehicleId, status);
                return ResponseEntity.ok(new TokenResponse(1, "Cập nhật trạng thái xe thành công", ""));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TokenResponse(0, "Không có quyền cập nhật trạng thái xe", ""));
            }
        }
    }
}
