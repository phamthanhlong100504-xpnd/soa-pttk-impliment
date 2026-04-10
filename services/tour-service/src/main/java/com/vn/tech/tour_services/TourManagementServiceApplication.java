package com.vn.tech.tour_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.vn.tech.tour_services.repository")
public class TourManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourManagementServiceApplication.class, args);
	}

}
