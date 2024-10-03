package org.example.server.Controller.Api;

import org.example.server.Model.Login;
import org.example.server.Model.Response.LoginResponse;
import org.example.server.Model.Response.RegisterResponse;
import org.example.server.Model.Response.TokenResponse;
import org.example.server.Model.User;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.example.server.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final JwtEncodeService jwtEncodeService;
    private final JwtDecodeService jwtDecodeService;
    @Autowired
    private UserService userService;

    public UserController(JwtEncodeService jwtEncodeService, JwtDecodeService jwtDecodeService) {
        this.jwtEncodeService = jwtEncodeService;
        this.jwtDecodeService = jwtDecodeService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Login login) {
        int isValid = userService.checkLogin(login.getPhoneNumber(), login.getPassword());
        if(isValid==1) {
            int userId = userService.getUserByPhoneNumber(login.getPhoneNumber()).getUserId();
            String token = jwtEncodeService.generateToken(userId,login.getPhoneNumber()); // giả sử bạn có jwtService để tạo token
            LoginResponse loginResponse = new LoginResponse("Đăng nhập thành công", 1, token);

            return loginResponse;
        }
        else if (isValid==2) {
            LoginResponse loginResponse = new LoginResponse("Sai mật khẩu", 2);
            return loginResponse;
        }
        else {
            LoginResponse loginResponse = new LoginResponse("Tài khoản không tồn tại", 0);
            return loginResponse;
        }
    }

    @PostMapping("/register")
    public RegisterResponse register(
            @RequestBody User user) {

        // Kiểm tra tính hợp lệ của dữ liệu
        if (user.getFullname().isEmpty() || user.getPassword().isEmpty() || user.getPhoneNumber().isEmpty()) {
            return new RegisterResponse("Các trường fullname, email, password, và phoneNumber không được để trống.", 0);
        }

//        // Kiểm tra số điện thoại
//        if (!user.getPhoneNumber().matches("^\\d{10}$")) { // Giả định số điện thoại có độ dài từ 10 đến 15 chữ số
//            return new RegisterResponse("Định dạng số điện thoại không hợp lệ.", 0);
//        }

        // Kiểm tra số điện thoại đã tồn tại
        if (userService.isPhoneExists(user.getPhoneNumber())) {
            return new RegisterResponse("Số điện thoại đã tồn tại.", 0);
        }

        // Thêm người dùng mới
        User user1 = new User();
        user1.setFullname(user.getFullname());
        user1.setBirthDay(user.getBirthDay()); // Sử dụng biến truyền vào
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword()); // Nên mã hóa mật khẩu trước khi lưu
        user1.setAddress(user.getAddress());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setRoleId(user.getRoleId());
        user1.setIsBlocked(user.getIsBlocked());
        user1.setLicenseNumber(user.getLicenseNumber());
        user1.setCompanyName(user.getCompanyName());
        user1.setBusCode(user.getBusCode());

        userService.addUser(user); // Thêm người dùng vào cơ sở dữ liệu

        return new RegisterResponse("Đăng ký thành công", 1);
    }

    @GetMapping("/validate")
    public TokenResponse validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("Token: " + authorizationHeader);
        if (authorizationHeader == null || authorizationHeader.isEmpty()){
            TokenResponse tokenResponse = new TokenResponse(0, "Token không hợp lệ","");
            return  tokenResponse;
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
            return new TokenResponse(0, "Token không hợp lệ", "");
        }
        String token = authorizationHeader.substring(7);

        if (jwtDecodeService.isTokenValid(token)){
            String phoneNumber = jwtDecodeService.extractPhoneNumber(token);
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if(user!=null){
                TokenResponse tokenResponse = new TokenResponse(1, "Token hợp lệ",user.getFullname());
                return tokenResponse;
            }
            else {
                TokenResponse tokenResponse = new TokenResponse(0, "Người dùng không tồn tại","");
                return tokenResponse;
            }
        }
        else {
            TokenResponse tokenResponse = new TokenResponse(0, "Token không hợp lệ", "");
            return tokenResponse;
        }
    }
}