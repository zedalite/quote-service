package de.zedalite.quotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * The QuotesApiApplication class is the entry point for the Quotes API application.
 * It is responsible for starting the Spring Boot application and enabling various features like
 * web security, caching, and scheduling.
 * <p>
 * The main() method is used to bootstrap the application by calling SpringApplication.run() with
 * the QuotesApiApplication class and command line arguments.
 * <p>
 * Usage:
 * 1. Start the application by running the main() method.
 * <p>
 * Example:
 * QuotesApiApplication.main(new String[] {});
 * <p>
 * Supported annotations:
 * - @SpringBootApplication: Enables autoconfiguration and component scanning.
 * - @EnableWebSecurity: Enables web security features.
 * - @EnableCaching: Enables caching support.
 * - @EnableScheduling: Enables scheduling capabilities.
 */
@SpringBootApplication
@EnableWebSecurity
@EnableCaching
@EnableScheduling
public class QuotesApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuotesApiApplication.class, args);
  }

}
