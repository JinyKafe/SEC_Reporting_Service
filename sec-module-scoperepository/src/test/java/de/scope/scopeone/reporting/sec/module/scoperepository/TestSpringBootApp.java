package de.scope.scopeone.reporting.sec.module.scoperepository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testcontainers.containers.MSSQLServerContainer;

@SpringBootApplication
public class TestSpringBootApp {

  private static final MSSQLServerContainer<?> SCOPEREPOSITORY_DB;

  static {
    SCOPEREPOSITORY_DB = new MSSQLServerContainer<>().withInitScript("scope-sec-db-schema.sql");
    SCOPEREPOSITORY_DB.start();

    System.setProperty("app.config.scoperepository.datasource.driverClassName", SCOPEREPOSITORY_DB.getDriverClassName());
    System.setProperty("app.config.scoperepository.datasource.jdbcUrl", SCOPEREPOSITORY_DB.getJdbcUrl());
    System.setProperty("app.config.scoperepository.datasource.username", SCOPEREPOSITORY_DB.getUsername());
    System.setProperty("app.config.scoperepository.datasource.password", SCOPEREPOSITORY_DB.getPassword());
    System.setProperty("spring.jpa.properties.hibernate.schema", "");
  }
}
