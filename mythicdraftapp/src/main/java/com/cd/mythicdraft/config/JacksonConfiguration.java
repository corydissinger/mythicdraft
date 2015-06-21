package com.cd.mythicdraft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan(basePackages = "com.cd.mythicdraft")
public class JacksonConfiguration {

	@Autowired
	private Environment environment;	
	
    @Bean
    public ObjectMapper mapper() {
    	return new ObjectMapper();
    }
}
