package com.wedding.backend.wedding_app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${wedding.api.key:}")
    private String apiKey;

    @Value("${wedding.api.enabled:true}")
    private boolean apiKeyEnabled;

    // Endpoints that don't require API key (like actuator, swagger-ui, health checks)
    private static final List<String> EXCLUDED_ENDPOINTS = List.of(
        "/actuator",
        "/swagger-ui",
        "/v3/api-docs",
        "/api-docs",           // SpringDoc OpenAPI docs
        "/swagger-resources",
        "/webjars",
        "/swagger-config",     // Swagger config endpoint
        "/favicon.ico"         // Browser favicon requests
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
        // Skip API key check if disabled (for local development)
        if (!apiKeyEnabled) {
            log.debug("API key authentication disabled");
            return true;
        }

        // Skip API key check for OPTIONS requests (CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String requestPath = request.getRequestURI();
        
        // Check if this is an excluded endpoint (actuator, swagger, etc.)
        if (isExcludedEndpoint(requestPath)) {
            log.debug("Excluded endpoint accessed: {}", requestPath);
            return true;
        }
        
        // All other endpoints require API key
        return validateApiKey(request, response, requestPath);
    }

    private boolean isExcludedEndpoint(String path) {
        return EXCLUDED_ENDPOINTS.stream()
                .anyMatch(path::startsWith);
    }

    private boolean validateApiKey(HttpServletRequest request, HttpServletResponse response, String path) {
        String providedKey = request.getHeader("X-API-Key");
        
        if (providedKey == null || providedKey.trim().isEmpty()) {
            log.warn("API key missing for endpoint: {} from IP: {}", path, getClientIP(request));
            sendUnauthorizedResponse(response, "API key required");
            return false;
        }

        // Decode base64 API key
        String decodedKey;
        try {
            decodedKey = new String(Base64.getDecoder().decode(providedKey));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid base64 API key format for endpoint: {} from IP: {}", path, getClientIP(request));
            sendUnauthorizedResponse(response, "Invalid API key format");
            return false;
        }

        // Validate API key
        if (!apiKey.equals(decodedKey)) {
            log.warn("Invalid API key for endpoint: {} from IP: {}", path, getClientIP(request));
            sendUnauthorizedResponse(response, "Invalid API key");
            return false;
        }

        log.debug("Valid API key provided for endpoint: {}", path);
        return true;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"error\":\"" + message + "\",\"status\":401}");
        } catch (Exception e) {
            log.error("Error writing unauthorized response", e);
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}