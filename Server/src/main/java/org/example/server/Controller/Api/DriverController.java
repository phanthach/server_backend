package org.example.server.Controller.Api;

import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.User;
import org.example.server.Service.DriverService;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.UserService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DriverController {
    private static final Logger log = LoggerFactory.getLogger(DriverController.class);
    private final JwtEncodeService jwtEncodeService;
    private final JwtDecodeService jwtDecodeService;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Autowired
    private UserService userService;
    @Autowired
    private DriverService driverService;

    public DriverController(JwtEncodeService jwtEncodeService, JwtDecodeService jwtDecodeService, PagedResourcesAssembler<User> pagedResourcesAssembler) {
        this.jwtEncodeService = jwtEncodeService;
        this.jwtDecodeService = jwtDecodeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/drivers")
    public ResponseEntity<?> getListDriver(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PageableDefault(size = 10) Pageable pageable) {
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
            String phoneNumber = jwtDecodeService.extractPhoneNumber(token);
            Page<User> users = driverService.getDriverByBusCode(phoneNumber, pageable);
            return ResponseEntity.ok(pagedResourcesAssembler.toModel(users));
        }
    }

    @GetMapping("/driver")
    public ResponseEntity<?> getLisDriver(@PageableDefault(size = 10) Pageable pageable) {
            Page<User> users = driverService.getDriverByBusCode("0854352262", pageable);
            log.info("oke" + users.toString());
            // Trả về danh sách tài xế
            return ResponseEntity.ok(pagedResourcesAssembler.toModel(users));
    }

    @DeleteMapping("/drivers/{phoneNumber}")
    public ResponseEntity<?> deleteDriver(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String phoneNumber) {
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
            String phoneNumberToken = jwtDecodeService.extractPhoneNumber(token);
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (phoneNumberToken.equals(user.getBusCode())) {
                driverService.deleteDriverByPhoneNumber(phoneNumber);
                return ResponseEntity.ok(new TokenResponse(1, "Xóa tài xế thành công", ""));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TokenResponse(0, "Không có quyền xóa tài xế", ""));
            }
        }
    }

    @PutMapping("/drivers/{phoneNumber}/block")
    public ResponseEntity<?> updateBlockDriver(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String phoneNumber, @RequestParam int isBlocked) {
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
            String phoneNumberToken = jwtDecodeService.extractPhoneNumber(token);
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (phoneNumberToken.equals(user.getBusCode())) {
                driverService.updateBlockeDriver(phoneNumber, isBlocked);
                return ResponseEntity.ok(new TokenResponse(1, "Cập nhật trạng thái tài xế thành công", ""));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TokenResponse(0, "Không có quyền cập nhật trạng thái tài xế", ""));
            }
        }
    }
}
