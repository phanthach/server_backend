package org.example.server.Controller.Api;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.server.Model.Layout;
import org.example.server.Model.Response.LayoutSeatResponse;
import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.Seat;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.LayoutService;
import org.example.server.Service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class LayoutController {
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private SeatService seatService;

    private JwtEncodeService jwtEncodeService;
    private JwtDecodeService jwtDecodeService;

    public LayoutController(JwtEncodeService jwtEncodeService, JwtDecodeService jwtDecodeService) {
        this.jwtEncodeService = jwtEncodeService;
        this.jwtDecodeService = jwtDecodeService;
    }

    @PostMapping("/addlayout")
    public ResponseEntity<?> addLayout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Layout layout) {
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
        if (layout != null && layout.getNameLayout() != null && layout.getSeatCapacity() > 0 && layout.getX() > 0 && layout.getY() > 0) {
            try {
                layout.setModId(jwtDecodeService.extractUserId(token));
                Layout savedLayout = layoutService.addLayout(layout);
                return ResponseEntity.ok(savedLayout);
            } catch (Exception e) {
                // Xử lý lỗi trong quá trình thêm layout
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponse(0, "Thêm layout thất bại: " + e.getMessage(), "", null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse(0, "Thông tin layout không hợp lệ", "", null));
        }
    }

    @GetMapping("/layouts")
    public ResponseEntity<?> getAllLayoutsByModId(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PageableDefault Pageable pageable) {
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
            Page<Layout> layouts = layoutService.getAllLayoutsByModId(modId, pageable);
            return ResponseEntity.ok(layouts);
        }
    }
    @GetMapping("/alllayouts")
    public ResponseEntity<?> getAllLayoutsByModId2(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PageableDefault Pageable pageable) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }
        log.info("modId: "+authorizationHeader);
        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Page<Layout> layouts = layoutService.getAllLayoutsByModId2(modId, pageable);
            return ResponseEntity.ok(layouts);
        }
    }
    @DeleteMapping("/deletelayout/{layoutId}")
    public ResponseEntity<?> deleteLayout(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PathVariable int layoutId) {
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
        int modId = jwtDecodeService.extractUserId(token);
        Layout layout = layoutService.getLayoutByLayoutId(layoutId);
        if(modId==layout.getModId()){
            seatService.deleteSeatsByLayoutId(layoutId);
            layoutService.deleteLayout(layoutId);
            return ResponseEntity.ok(new TokenResponse(1, "Xóa layout thành công", "", null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Không thể xóa Layout vì Layout đang được sử dụng", "", null));
    }
    @GetMapping("/getLayout/{layoutId}")
    public ResponseEntity<?> getLayoutByLayoutId(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PathVariable int layoutId) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);
        // Kiểm tra token hợp lệ
        try{
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "", null));
        } else {
            int modId = jwtDecodeService.extractUserId(token);
            Layout layout = layoutService.getLayoutByLayoutId(layoutId);
            if (layout != null) {
                if(modId != layout.getModId()){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TokenResponse(0, "Không có quyền truy cập layout", "", null));
                }
                return ResponseEntity.ok(layout);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TokenResponse(0, "Không tìm thấy layout", "", null));
            }
        }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponse(0, "Lỗi không xác định", "", null));
        }
    }
    @GetMapping("/getLayouts")
    public ResponseEntity<?> getLayouts(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @RequestParam int layoutId) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new LayoutSeatResponse("Token không hợp lệ",401,null, null,null,null,null,null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);
        log.info(authorizationHeader);
        // Kiểm tra token hợp lệ
        try{
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(401).body(new LayoutSeatResponse("Token không hợp lệ",401,null, null,null,null,null,null));
        } else {
            Layout layout = layoutService.getLayoutByLayoutId(layoutId);
            LayoutSeatResponse layoutSeatResponse = new LayoutSeatResponse();

            if(layout != null){
                layoutSeatResponse.setMessage("Lấy thông tin layout thành công");
                layoutSeatResponse.setStatus(200);
                layoutSeatResponse.setLayoutId(layoutId);
                layoutSeatResponse.setSeatCapacity(layout.getSeatCapacity());
                layoutSeatResponse.setX(layout.getX());
                layoutSeatResponse.setY(layout.getY());
                layoutSeatResponse.setFloor(layout.getFloor());
                List<Seat> seats = seatService.getAllSeats(layoutId);
                layoutSeatResponse.setListSeat(seats);
                return ResponseEntity.ok(layoutSeatResponse);
            }
            else {
                return ResponseEntity.status(400).body(new LayoutSeatResponse("Không có thông tin ghế",400,layoutId, null,null,null,null,null));
            }
        }
        }catch (Exception e){
            return ResponseEntity.status(401).body(new LayoutSeatResponse("Lỗi không xác định",401,null, null,null,null,null,null));
        }
    }
}
