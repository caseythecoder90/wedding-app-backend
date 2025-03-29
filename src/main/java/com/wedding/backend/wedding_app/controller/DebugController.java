package com.wedding.backend.wedding_app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/debug")
public class DebugController {

    @GetMapping("/cors")
    public Map<String, String> debugCors(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> debug = new HashMap<>();
        debug.put("origin", request.getHeader("Origin"));
        debug.put("cors_enabled", "true");
        return debug;
    }
}