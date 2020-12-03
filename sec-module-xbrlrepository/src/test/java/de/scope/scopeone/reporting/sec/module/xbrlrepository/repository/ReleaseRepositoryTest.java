package de.scope.scopeone.reporting.sec.module.xbrlrepository.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
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
public class ReleaseRepositoryTest {

  private static final String firstId = "4264e8c9-403d-461e-96e0-129a2050f622";
  private static final String secondId = "9234e8c9-403d-461e-96e0-174a2050f511";
  private static final String jobId =    "0e8d0e38-e153-44a0-b1a2-d1980471bb5e";
  @Autowired
  private ReleaseRepository releaseRepository;

  @Test
  public void loadAll() {
    List<Release> releases = releaseRepository.loadAll();

    assertThat(releases).isNotNull().size().isEqualTo(2);
  }

  @Test
  public void loadById() {
    Release release = releaseRepository.load(firstId);

    assertThat(release).isNotNull();
    assertThat(release.getId()).isEqualTo(firstId);
    assertThat(release.getJobId()).isEqualTo(jobId);
  }

  @Test
  public void loadByJobId() {
    List<Release> releases = releaseRepository.loadByJobId(jobId);

    assertThat(releases).isNotNull().size().isEqualTo(2);
    assertThat(releases).extracting("jobId").containsOnly(jobId);
  }

  @Test
  public void loadWithNonExistingId() {
    loadNonExixtingReleaseById(UUID.randomUUID().toString());

  }

  @Test
  public void loadWithNonExistingJobId() {
    List<Release> releases = releaseRepository.loadByJobId(UUID.randomUUID().toString());
    assertThat(releases).isNotNull().isEmpty();

  }

  @Test
  public void loadByReleaseDateDesc() {
    List<Release> releases = releaseRepository.loadByReleaseDateDesc(0, 10);
    assertThat(releases).isNotNull().size().isEqualTo(2);
    assertThat(releases).extracting("id").contains(firstId, secondId);
    assertThat(releases.get(0)).extracting("id").isEqualTo(firstId);

  }

  @Test
  public void loadByReleaseDateDescWithNonExistingData() {
    List<Release> releases = releaseRepository.loadByReleaseDateDesc(10, 10);
    assertThat(releases).isNotNull().isEmpty();

  }

  @Test
  public void saveAndDelete() {
    Release release = Release.builder().id(UUID.randomUUID().toString())
        .jobId(jobId)
        .releaseDate(LocalDate.now())
        .content("".getBytes())
        .fileName("newFile")
        .state("Empty")
        .build();

    loadNonExixtingReleaseById(release.getId());
    releaseRepository.save(release);
    Release savedRelease = releaseRepository.load(release.getId());
    assertThat(savedRelease.getId()).isEqualTo(release.getId());
    releaseRepository.delete(release.getId());
    loadNonExixtingReleaseById(release.getId());

  }

  private void loadNonExixtingReleaseById(String id) {
    try {
      releaseRepository.load(id);
      fail("exception org.springframework.dao.EmptyResultDataAccessException expected");
    } catch (EmptyResultDataAccessException e) {

    }
  }

}
