package de.zedproject.quotesapi;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class DatabaseContainerBaseTest {

  static final PostgreSQLContainer<?> DB_CONTAINER;

  static {
    DB_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
      .withInitScript("postgres-quote-database.sql");
    DB_CONTAINER.start();
  }

  @DynamicPropertySource
  static void registerMySQLProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
    registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
  }
}

