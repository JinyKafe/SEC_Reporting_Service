package de.scope.scopeone.reporting.sec.module.xbrlrepository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testcontainers.containers.MSSQLServerContainer;

@SpringBootApplication
public class TestSpringBootApp {

  private static final MSSQLServerContainer<?> XBRLREPOSITORY_DB;

  static {
    XBRLREPOSITORY_DB = new MSSQLServerContainer<>();
    XBRLREPOSITORY_DB.start();

    System.setProperty("app.config.xbrlrepository.datasource.driverClassName", XBRLREPOSITORY_DB.getDriverClassName());
    System.setProperty("app.config.xbrlrepository.datasource.jdbcUrl", XBRLREPOSITORY_DB.getJdbcUrl());
    System.setProperty("app.config.xbrlrepository.datasource.username", XBRLREPOSITORY_DB.getUsername());
    System.setProperty("app.config.xbrlrepository.datasource.password", XBRLREPOSITORY_DB.getPassword());
    System.setProperty("app.config.xbrlrepository.datasource.schema", "");
  }
}
