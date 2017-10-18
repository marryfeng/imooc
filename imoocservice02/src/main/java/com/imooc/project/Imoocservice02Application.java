package com.imooc.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Imoocservice02Application {

	public static void main(String[] args) {
		SpringApplication.run(Imoocservice02Application.class, args);
	}
}
