package org.example.server.Controller.Api;

import org.example.server.Model.Layout;
import org.example.server.Model.Location;
import org.example.server.Model.Response.RouteAndLocationsResponse;
import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.Route;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.RouteService;
import org.example.server.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RouteController {
    private static final Logger log = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private RouteService routeService;
    @Autowired
    private UserService userService;

    private JwtDecodeService jwtDecodeService;
    private JwtEncodeService jwtEncodeService;

    public RouteController(JwtDecodeService jwtDecodeService, JwtEncodeService jwtEncodeService) {
        this.jwtDecodeService = jwtDecodeService;
        this.jwtEncodeService = jwtEncodeService;
    }

    @PostMapping("/addroute")
    public ResponseEntity<?> addRoute(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Route route) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);
        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        }

        // Kiểm tra đối tượng layout không null và các thuộc tính cần thiết
        if (route != null && route.getStartLocation() != null && route.getEndLocation()!=null ) {
            try {
                route.setModId(jwtDecodeService.extractUserId(token));
                Route savedRoute = routeService.addRoute(route);
                return ResponseEntity.ok(savedRoute);
            } catch (Exception e) {
                // Xử lý lỗi trong quá trình thêm layout
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponse(0, "Thêm route thất bại: " + e.getMessage(), "", null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse(0, "Thông tin route không hợp lệ", "", null));
        }
    }
    @GetMapping("/routes")
    public ResponseEntity<?> getAllRoutesByModId(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PageableDefault Pageable pageable) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Page<Route> routes = routeService.getAllRoutesByModId(modId, pageable);
            log.info("routes: " + routes);
            return ResponseEntity.ok(routes);
        }
    }
    @DeleteMapping("/deleteroute/{routeId}")
    public ResponseEntity<?> deleteRoute(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PathVariable int routeId) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);
        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        }
        else{
            int modId = jwtDecodeService.extractUserId(token);
            Route route = routeService.getRouteByRouteId(routeId);

            if(route==null || route.getModId()!=modId){
                return ResponseEntity.status(403).body("Bạn không có quyền xóa sơ đồ ghế này");
            }
            else{
                try{
                    routeService.deleteRouteByRouteId(routeId);
                    return ResponseEntity.ok().body("Xóa route thành công");
                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponse(0, "Xóa route thất bại: " + e.getMessage(), "",null));
                }
            }
        }
    }
    @GetMapping("/getroute")
    public ResponseEntity<?> getAllLocationsByRouteId(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestParam int routeId) {
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if(!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        }
        else {
            int modId = jwtDecodeService.extractUserId(token);
            Route route = routeService.getRouteByRouteId(routeId);
            if(route==null || route.getModId()!=modId) {
                return ResponseEntity.status(403).body("Bạn không có quyền xem vị trí của tuyến đường này");
            }
            else{
                return ResponseEntity.ok(route);
            }
        }
    }
}
