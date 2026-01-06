package org.example.server.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "doald2ki9");
        config.put("api_key", "813647613417613");
        config.put("api_secret", "-Ov3zz6-H2pH7eHqdz778_hxwec");
        return new Cloudinary(config);
    }
}
