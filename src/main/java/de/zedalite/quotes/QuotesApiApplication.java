package de.zedalite.quotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
@EnableScheduling
public class QuotesApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuotesApiApplication.class, args);
  }

}
