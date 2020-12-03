package de.scope.scopeone.reporting.sec.module.scoperepository.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/scope-sec-db-sample-data.sql"})
class IssuerJpaServiceImplTest {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  @Autowired
  private IssuerJpaService issuerJpaService;

  @Test
  void contextLoads() {
    // initialization test
    assertThat(issuerJpaService).isNotNull();
  }

  @Test
  void findIssuerById() {
    Issuer issuer = issuerJpaService.findIssuerById(25410);
    assertThat(issuer).isNotNull();
  }

  @Test
  void testErrorValidation() {
    // issuer#name must not be null
    jdbcTemplate.execute("update issuer set name = null where id = '25410'");
    // for issuer#492104 : instrument_rating_action#rating must not be null
    jdbcTemplate.execute("update instrument_rating_action set rating = null where instrument_id = '563661'");
    assertThatThrownBy(() -> issuerJpaService.findIssuerById(25410)).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("findIssuerById.<return value>.name:");
    assertThatThrownBy(() -> issuerJpaService.findIssuerById(492104)).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("findIssuerById.<return value>");
  }
}