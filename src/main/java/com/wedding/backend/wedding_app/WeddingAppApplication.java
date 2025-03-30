package com.wedding.backend.wedding_app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@EnableJpaRepositories
@SpringBootApplication
public class WeddingAppApplication {

	private static final Logger log = LoggerFactory.getLogger(WeddingAppApplication.class);

	public static void main(String[] args) {
		log.info("Starting Casey & Yasmim's backend wedding app....");
		SpringApplication.run(WeddingAppApplication.class, args);
		log.info("Casey & Yasmim's backend wedding app started successfully!");
	}

}
