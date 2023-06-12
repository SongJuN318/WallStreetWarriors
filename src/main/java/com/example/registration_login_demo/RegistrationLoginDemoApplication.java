package com.example.registration_login_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("com.example.registration_login_demo.entity")
public class RegistrationLoginDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationLoginDemoApplication.class, args);
	}
}
