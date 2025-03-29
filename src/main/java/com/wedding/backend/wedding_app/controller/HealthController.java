package com.wedding.backend.wedding_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/api/health")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);


    @GetMapping("/isAlive")
    public ResponseEntity<String> isAlive() {

        log.info("BEGIN - Health check endpoint");

        ResponseEntity<String> healthResponse = new ResponseEntity<>("I am alive", HttpStatus.OK);

        log.info("END - Health check endpoint");

        return healthResponse;

    }


}
