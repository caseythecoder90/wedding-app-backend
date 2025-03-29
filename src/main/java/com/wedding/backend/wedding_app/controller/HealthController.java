package com.wedding.backend.wedding_app.controller;

import com.wedding.model.reponse.HealthCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*") // Allow all origins (for development)
@RequestMapping("v1/api/health")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/isAlive")
    @ResponseBody
    public ResponseEntity<HealthCheckResponse> isAlive() {

        log.info("BEGIN - Health check endpoint");

        HealthCheckResponse healthCheckResponse = HealthCheckResponse.builder()
                .status("SUCCESS")
                .message("I AM ALIVE")
                .build();

        ResponseEntity<HealthCheckResponse> healthResponse =
                new ResponseEntity<>(healthCheckResponse, HttpStatus.OK);

        log.info("END - Health check endpoint");

        return healthResponse;

    }


}
