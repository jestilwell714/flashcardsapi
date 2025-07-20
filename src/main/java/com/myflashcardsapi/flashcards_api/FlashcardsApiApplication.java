package com.myflashcardsapi.flashcards_api;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log
public class  FlashcardsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashcardsApiApplication.class, args);
	}

}
