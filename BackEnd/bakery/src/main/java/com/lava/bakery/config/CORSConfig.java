package com.lava.bakery.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;
@Configuration
public class CORSConfig {

    @Value("${cors.allowed.origins}")
    private String[] origins;

    @Value("${cors.allowed.methods}")
    private String[] methods;

    @Value("${cors.allowed.headers}")
    private String[] headers;

    @Value("${cors.allowed.credentials}")
    private boolean credentials;

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods(methods)
                        .allowedHeaders(headers)
                        .allowCredentials(credentials);
            }
        };
    }
}
