package com.devbank.DevBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DevBankApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("api.security.token.secret", dotenv.get("API_SECURITY_TOKEN_SECRET"));
		System.setProperty("spring.mail.username", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("spring.mail.password", dotenv.get("SPRING_MAIL_PASSWORD"));
		System.setProperty("spring.mail.host", dotenv.get("SPRING_MAIL_HOST"));
		System.setProperty("spring.mail.port", dotenv.get("SPRING_MAIL_PORT"));
		System.setProperty("spring.profiles.active", dotenv.get("SPRING_PROFILES_ACTIVE"));

		if ("dev".equals(dotenv.get("SPRING_PROFILES_ACTIVE"))) {
		    System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
		    System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		    System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		}

		SpringApplication.run(DevBankApplication.class, args);
	}

}
