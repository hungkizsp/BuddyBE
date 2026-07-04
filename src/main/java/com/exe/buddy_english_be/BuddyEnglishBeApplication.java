package com.exe.buddy_english_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuddyEnglishBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuddyEnglishBeApplication.class, args);
	}

}
