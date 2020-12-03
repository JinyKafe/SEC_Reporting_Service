package de.scope.scopeone.reporting.sec.module.scoperepository.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
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
public class ObligorJpaServiceImplTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  private ObligorJpaService obligorJpaService;

  @Test
  public void contextLoads() {
    // initialization test
    assertThat(obligorJpaService).isNotNull();
  }

  @Test
  public void findObligorById() {
    Obligor obligor = obligorJpaService.findObligorById(25410);
    assertThat(obligor).isNotNull();
  }

  @Test
  void testErrorValidation() {
    // set null to one of the mandatory fields to evoke an error
    jdbcTemplate.execute("update obligor set name = null where id = '25410'");
    assertThatThrownBy(() -> obligorJpaService.findObligorById(25410)).isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("findObligorById.<return value>.name:");
  }
}