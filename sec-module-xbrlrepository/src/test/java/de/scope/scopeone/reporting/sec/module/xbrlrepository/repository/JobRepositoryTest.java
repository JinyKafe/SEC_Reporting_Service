package de.scope.scopeone.reporting.sec.module.xbrlrepository.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.JobStateType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/xbrl-sec-db-sample-data.sql"})
public class JobRepositoryTest {

  private static final String jobId = "0e8d0e38-e153-44a0-b1a2-d1980471bb5e";
  @Autowired
  private JobRepository jobRepository;

  @Test
  public void loadById() {
    Job job = jobRepository.load(jobId);

    assertThat(job).isNotNull();
    assertThat(job.getId()).isEqualTo(job.getId());
  }


  @Test
  public void loadWithNonExistingId() {
    loadNonExistingById(UUID.randomUUID().toString());
  }


  @Test
  public void loadByReleaseDateDesc() {
    List<Job> jobs = jobRepository.loadByDateDesc(0, 10);
    assertThat(jobs).isNotNull().size().isEqualTo(1);
    assertThat(jobs.get(0)).extracting("id").isEqualTo(jobId);
  }

  @Test
  public void loadByReleaseDateDescWithNonExistingData() {
    List<Job> jobs = jobRepository.loadByDateDesc(10, 10);
    assertThat(jobs).isNotNull().isEmpty();
  }

  @Test
  public void saveAndDelete() {
    Job job = Job.builder()
        .id(UUID.randomUUID().toString())
        .executionDate(LocalDate.now())
        .state(JobStateType.FINISHED)
        .build();

    loadNonExistingById(job.getId());
    jobRepository.save(job);
    Job savedJob = jobRepository.load(job.getId());
    assertThat(savedJob.getId()).isEqualTo(job.getId());
    jobRepository.delete(job.getId());
    loadNonExistingById(job.getId());

  }

  private void loadNonExistingById(String id) {
    try {
      jobRepository.load(id);
      fail("exception org.springframework.dao.EmptyResultDataAccessException expected");
    } catch (EmptyResultDataAccessException e) {

    }
  }

}
