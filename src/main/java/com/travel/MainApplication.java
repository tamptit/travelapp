package com.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	@Scope(value = "singleton")
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
}
