package de.scope.scopeone.reporting.sec.module.xbrlrepository.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/xbrl-sec-db-sample-data.sql"})
class XbrlRepositoryServiceImplTest {

  @Autowired
  private XbrlRepositoryService xbrlRepositoryService;

  @Test
  void contextLoads() {
    //  JdbcTestUtils
    assertThat(xbrlRepositoryService).isNotNull();
  }
}