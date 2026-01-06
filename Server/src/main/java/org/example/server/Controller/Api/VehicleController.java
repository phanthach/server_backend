package org.example.server.Controller.Api;

import org.example.server.Model.Layout;
import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.Response.VehicleResponse;
import org.example.server.Model.User;
import org.example.server.Model.Vehicle;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.LayoutService;
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

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private LayoutService layoutService;

    public VehicleController(JwtDecodeService jwtDecodeService, JwtEncodeService jwtEncodeService) {
        this.jwtDecodeService = jwtDecodeService;
        this.jwtEncodeService = jwtEncodeService;
    }

    @GetMapping("/vehicles")
    public ResponseEntity<?> getListVehicle(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PageableDefault(size = 10) Pageable pageable) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(401).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Page<Vehicle> vehicles = vehicleService.getVehicleByModId(modId, pageable);

            return ResponseEntity.ok(vehicles);
        }
    }
    @PutMapping("/vehicles/{vehicleId}/block")
    public ResponseEntity<?> updateBlockDriver(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable int vehicleId, @RequestParam int status) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        } else {
            int userId = jwtDecodeService.extractUserId(token);
            Vehicle vehicle = vehicleService.getVehecleByVehicleId(vehicleId);
            if (userId==vehicle.getModId()) {
                vehicleService.updateStatus(vehicleId, status);
                return ResponseEntity.ok(new TokenResponse(1, "Cập nhật trạng thái xe thành công", "",null));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TokenResponse(0, "Không có quyền cập nhật trạng thái xe", "",null));
            }
        }
    }

    @PostMapping("/addvehicles")
    public ResponseEntity<?> addVehicle(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Vehicle vehicle) {
        // Kiểm tra header Authorization
        log.info(vehicle.toString());
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }

        // Kiểm tra đối tượng vehicle không null và các thuộc tính cần thiết
        if (vehicle != null) {
            if(vehicle.getPlateNumber()==null || vehicle.getVehicleType()==null || vehicle.getImg()==null || vehicle.getLayoutId()==0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse(0, "Thông tin vehicle không hợp lệ", "",null));
            }

            try {
                int checkVehicleExist = vehicleService.checkVehicleExist(vehicle.getPlateNumber());
                if (checkVehicleExist > 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse(0, "Biển số xe đã tồn tại", "",null));
                }
                else {
                    vehicle.setModId(jwtDecodeService.extractUserId(token));
                    vehicleService.addVehicle(vehicle);
                    return ResponseEntity.ok(new TokenResponse(1, "Thêm vehicle thành công", "",null));
                }
            } catch (Exception e) {
                // Xử lý lỗi trong quá trình thêm vehicle
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponse(0, "Thêm vehicle thất bại: " + e.getMessage(), "",null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse(0, "Thông tin vehicle không hợp lệ", "",null));
        }
    }
    @GetMapping("/listVehicle")
    public ResponseEntity<?> getVehicle(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PageableDefault(size = 10) Pageable pageable){
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }
        int modId = jwtDecodeService.extractUserId(token);
        Page<VehicleResponse> vehicleResponses = vehicleService.getVehicleResponse(modId, pageable);
        return ResponseEntity.ok(vehicleResponses);
    }
}
