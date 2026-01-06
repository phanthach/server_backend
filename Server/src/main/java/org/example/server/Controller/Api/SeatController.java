package org.example.server.Controller.Api;

import org.example.server.Model.Layout;
import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.Seat;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.LayoutService;
import org.example.server.Service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SeatController {
    private static final Logger log = LoggerFactory.getLogger(SeatController.class);
    @Autowired
    private SeatService seatService;
    @Autowired
    private LayoutService layoutService;
    private JwtEncodeService jwtEncodeService;
    private JwtDecodeService jwtDecodeService;

    public SeatController(JwtEncodeService jwtEncodeService, JwtDecodeService jwtDecodeService) {
        this.jwtEncodeService = jwtEncodeService;
        this.jwtDecodeService = jwtDecodeService;
    }

    @GetMapping("/listseat/{layoutId}")
    public ResponseEntity<?> listSeat(@PathVariable int layoutId) {
        List<Seat> seats = seatService.getAllSeats(layoutId);
        if (seats == null || seats.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy ghế");
        }
        return ResponseEntity.ok(seats);
    }
    @PostMapping("/addseat")
    public ResponseEntity<?> addSeat(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody Seat seat) {
        log.info(seat.toString());
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        String token = authorizationHeader.substring(7);
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(403).body("Vui lòng đăng nhập lại");
        }
        else{
            int modId = jwtDecodeService.extractUserId(token);
            int layoutId = seat.getLayoutId();
            Layout layout = layoutService.getLayoutByLayoutId(layoutId);
            long coutSeat = seatService.countSeatsByLayoutId(layoutId);
            boolean checkExitsName = seatService.existsByNameSeatAndLayoutId(seat.getNameSeat(),layoutId);
            boolean checkExitsInfo = seatService.existsByPositionXAndPositionYAndFloorAndLayoutId(seat.getPosition_x(),seat.getPosition_y(),seat.getFloor(),layoutId);
            if(layout==null || layout.getModId()!=modId){
                return ResponseEntity.status(403).body("Bạn không có quyền chỉnh sửa sơ đồ ghế này");
            }
            else if(checkExitsName==true || checkExitsInfo == true || seat.getNameSeat()==null || seat.getPosition_x()<0 || seat.getPosition_y()<0 || seat.getFloor()<0 || seat.getPosition_x()>layout.getX() || seat.getPosition_y()>layout.getY()){
                log.info("Lỗi đây"+ seat.getPositionY() +" "+layout.getY());
                return ResponseEntity.status(400).body("Dữ liệu không hợp lệ, vui lòng kiểm tra lại");
            } else if(coutSeat == layout.getSeatCapacity()) {
                return ResponseEntity.status(400).body("Đã đủ số lượng ghế khai báo, không thể thêm mới ghế");
            }
            else{
                seatService.addSeat(seat);
                if(coutSeat+1 == layout.getSeatCapacity()){
                    layoutService.updateStatusByLayoutId(layoutId, 0);
                }
                return ResponseEntity.ok().body("Thêm ghế thành công");
            }
        }
    }
    @DeleteMapping("/deleteseat/{seatId}")
    public ResponseEntity<?> deleteLayout(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PathVariable int seatId) {
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
            seatService.deleteSeat(seatId);
            return ResponseEntity.ok(new TokenResponse(1, "Xóa Seat thành công", "",null));
        }
    }
    @GetMapping("/countSeats/{layoutId}")
    public ResponseEntity<?> checkLayoutStatus(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @PathVariable int layoutId) {
        // Kiểm tra header Authorization
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }

        // Lấy token sau "Bearer "
        String token = authorizationHeader.substring(7);

        // Kiểm tra token hợp lệ
        if (!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(0, "Token không hợp lệ", "",null));
        }
        long count = seatService.countSeatsByLayoutId(layoutId);
        log.info("count: "+ count);
        Layout layout = layoutService.getLayoutByLayoutId(layoutId);
        if(count==layout.getSeatCapacity()){
            layoutService.updateStatusByLayoutId(layoutId,0);
        }
        else{
            layoutService.updateStatusByLayoutId(layoutId,1);
        }
        return ResponseEntity.ok(count);
    }
}
