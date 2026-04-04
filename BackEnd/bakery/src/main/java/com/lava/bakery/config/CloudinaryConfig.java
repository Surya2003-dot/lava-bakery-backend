package com.lava.bakery.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", System.getenv("CLOUD_NAME"),
                "api_key", System.getenv("API_KEY"),
                "api_secret", System.getenv("API_SECRET")
        ));
    }
}