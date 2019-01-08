package com.jabejokumparan.newsandtopic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NewsandtopicApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsandtopicApplication.class, args);
	}

}
