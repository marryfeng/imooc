package com.imooc.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Imoocservice01Application {

	public static void main(String[] args) {
		SpringApplication.run(Imoocservice01Application.class, args);
	}
	/*//配置mybatis的分页插件pageHelper
     @Bean
     public PageHelper pageHelper() {
		 PageHelper pageHelper = new PageHelper();
		 Properties properties = new Properties();
		 properties.setProperty("offsetAsPageNum", "true");
		 properties.setProperty("rowBoundsWithCount", "true");
		 properties.setProperty("reasonable", "true");
		 properties.setProperty("dialect", "mysql");    //配置mysql数据库的方言
		 pageHelper.setProperties(properties);
		 return pageHelper;
	 }*/
}
