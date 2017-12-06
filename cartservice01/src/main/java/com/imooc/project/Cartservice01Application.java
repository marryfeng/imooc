package com.imooc.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Cartservice01Application {

	public static void main(String[] args) {
		SpringApplication.run(Cartservice01Application.class, args);
	}
}
