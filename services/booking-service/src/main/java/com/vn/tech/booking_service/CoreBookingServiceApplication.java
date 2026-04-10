package com.vn.tech.booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class CoreBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBookingServiceApplication.class, args);
	}

}
