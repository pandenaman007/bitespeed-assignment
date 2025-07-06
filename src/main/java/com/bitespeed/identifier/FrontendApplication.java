package com.bitespeed.identifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
		System.out.println("✅ Spring Boot app started successfully on http://localhost:8081");
	}

}
