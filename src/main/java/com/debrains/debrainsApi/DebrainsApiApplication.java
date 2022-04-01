package com.debrains.debrainsApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DebrainsApiApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "optional:classpath:application.yml,"
			+ "optional:/usr/local/debrainsApi/application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(DebrainsApiApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
