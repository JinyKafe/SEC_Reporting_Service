package de.scope.scopeone.reporting.sec.app;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SqlGroup({
    @Sql(scripts = "classpath:/scope-sec-db-sample-data.sql", config = @SqlConfig(dataSource = "scopeRepositoryDataSource", transactionManager = "scoperepositoryTransatctionManager"))
})
public abstract class BaseIntegrationTest {

  private static final GenericContainer<?> REDIS;
  private static final MSSQLServerContainer<?> SCOPEREPOSITORY_DB;
  private static final MSSQLServerContainer<?> XBRLREPOSITORY_DB;

  private static final String REDISPASS = "pass";

  protected TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Value("${server.internal.http.auth.username}")
  private String username;

  @Value("${server.internal.http.auth.password}")
  private String password;

  @BeforeEach
  public void setUp() {
    if (restTemplate == null) {
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().basicAuthentication(username
          , password).rootUri(String.format("http://localhost:%d", port));
      restTemplate = new TestRestTemplate(restTemplateBuilder);
    }

  }

  static {
    // Initialize Redis container
    REDIS = new GenericContainer("redis:latest").withCommand("redis-server", "--requirepass", REDISPASS).withExposedPorts(6379);
    REDIS.start();
    System.setProperty("spring.redis.host", REDIS.getContainerIpAddress());
    System.setProperty("spring.redis.password", REDISPASS);
    System.setProperty("spring.redis.port", REDIS.getFirstMappedPort().toString());

    // Initialize Scope repository DB container
    SCOPEREPOSITORY_DB = new MSSQLServerContainer<>().withInitScript("scope-sec-db-schema.sql");
    SCOPEREPOSITORY_DB.start();
    System.setProperty("app.config.scoperepository.datasource.driverClassName", SCOPEREPOSITORY_DB.getDriverClassName());
    System.setProperty("app.config.scoperepository.datasource.jdbcUrl", SCOPEREPOSITORY_DB.getJdbcUrl());
    System.setProperty("app.config.scoperepository.datasource.username", SCOPEREPOSITORY_DB.getUsername());
    System.setProperty("app.config.scoperepository.datasource.password", SCOPEREPOSITORY_DB.getPassword());

    // Initialize XBRL repository DB container
    XBRLREPOSITORY_DB = new MSSQLServerContainer<>();
    XBRLREPOSITORY_DB.start();
    System.setProperty("app.config.xbrlrepository.datasource.driverClassName", XBRLREPOSITORY_DB.getDriverClassName());
    System.setProperty("app.config.xbrlrepository.datasource.jdbcUrl", XBRLREPOSITORY_DB.getJdbcUrl());
    System.setProperty("app.config.xbrlrepository.datasource.username", XBRLREPOSITORY_DB.getUsername());
    System.setProperty("app.config.xbrlrepository.datasource.password", XBRLREPOSITORY_DB.getPassword());
    System.setProperty("app.config.xbrlrepository.datasource.schema", "");
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
  public int getPort(){
    return port;
  }

}
