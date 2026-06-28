package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerEurekaApplication.class, args);
	}

}
//kama
/*
http://localhost:8761

We use Eureka as service registry.
All microservices register themselves and API Gateway uses logical service names instead of hardcoded URLs.
*/