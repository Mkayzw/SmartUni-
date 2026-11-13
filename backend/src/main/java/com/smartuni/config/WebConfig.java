package com.smartuni.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS for local development: the Vite dev server (port 5173) calls the API on port 8080.
 * Browsers treat those as different origins and block the response unless the server opts in.
 *
 * (The Vite config also proxies /api, so you can use either approach. Having both is deliberate:
 * see INTERVIEW_THEORY.md, "What is CORS and who enforces it?")
 *
 * @Configuration marks a class that contributes configuration to the Spring container.
 * WebMvcConfigurer is a callback interface: Spring MVC finds every such bean and calls it.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
