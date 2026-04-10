package com.vn.tech.booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableDiscoveryClient
@EnableFeignClients
public class CoreBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBookingServiceApplication.class, args);
	}

}
