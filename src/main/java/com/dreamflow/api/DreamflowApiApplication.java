package com.dreamflow.api;

import com.dreamflow.api.messaging.email.EmailMessage;
import com.dreamflow.api.messaging.email.interfaces.EmailMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
// import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class DreamflowApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(DreamflowApiApplication.class, args);
	}
}
