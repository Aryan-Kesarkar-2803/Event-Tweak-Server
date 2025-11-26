package com.example.EventTweak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventTweakApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTweakApplication.class, args);
	}
}
