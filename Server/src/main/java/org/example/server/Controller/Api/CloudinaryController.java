package org.example.server.Controller.Api;

import org.example.server.Service.CloudinaryService;
import org.example.server.Service.JwtDecodeService;
import org.example.server.Service.JwtEncodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {
    private JwtDecodeService jwtDecodeService;
    private JwtEncodeService jwtEncodeService;
    @Autowired
    private CloudinaryService cloudinaryService;

    public CloudinaryController(JwtDecodeService jwtDecodeService, JwtEncodeService jwtEncodeService) {
        this.jwtDecodeService = jwtDecodeService;
        this.jwtEncodeService = jwtEncodeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<?,?>> uploadFileImage(@RequestHeader(value = "Authorization",required = false ) String authorizationHeader, @RequestParam("file")MultipartFile file) throws Exception {
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("message","Token không hợp lệ"));
        }
        String token = authorizationHeader.substring(7);

        if(!jwtDecodeService.isTokenValid(token)) {
            return ResponseEntity.badRequest().body(Map.of("message","Token không hợp lệ"));
        }
        else {
            Map<?,?> result = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(result);
        }
    }
}
