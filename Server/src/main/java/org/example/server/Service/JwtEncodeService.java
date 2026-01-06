package org.example.server.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtEncodeService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Chuyển chuỗi secretKey thành đối tượng SecretKey
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo token từ userId (int) và số điện thoại
    public String generateToken(int userId, String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);  // Thêm userId vào claims dưới dạng số nguyên
        return createToken(claims, phoneNumber);
    }

    // Tạo token với claims và subject (ở đây là phoneNumber)
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token hết hạn sau 10 giờ
                .signWith(secretKey)
                .compact();
    }
}
