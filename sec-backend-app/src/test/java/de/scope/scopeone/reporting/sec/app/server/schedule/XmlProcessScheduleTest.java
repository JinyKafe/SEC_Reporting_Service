package de.scope.scopeone.reporting.sec.app.server.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.scope.scopeone.reporting.sec.app.BaseIntegrationTest;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReleaseRepository;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReportRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.awaitility.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@TestPropertySource(properties = {"app.config.schedule.time=0/1 * * * * ?"})
@Sql(scripts = "classpath:/xbrl-sec-db-clear-data.sql", config = @SqlConfig(dataSource = "xbrlRepositoryDataSource", transactionManager = "xbrlRepositoryTransactionManager"))
public class XmlProcessScheduleTest extends BaseIntegrationTest {

  private final String KEYPREFIX = "job-lock";
  @Value("${app.config.schedule.name}")
  private String name;
  @Value("${app.config.schedule.env}")
  private String env;
  @SpyBean
  private XmlProcessSchedule XmlProcessSchedule;

  @Autowired
  private ReleaseRepository releaseRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Test
  @DisplayName("Scheduled Process for triggerXmlProcess")
  public void scheduledProcess() throws IOException {

    await().atMost(Duration.ONE_MINUTE)
        .untilAsserted(() -> verify(XmlProcessSchedule, times(1)).triggerXmlProcess());

    assertThat(redisTemplate.hasKey(getKeyName())).isTrue();

    await().atMost(Duration.FIVE_MINUTES)
        .until(() -> !releaseRepository.loadAll().isEmpty());

    List<Release> releases = releaseRepository.loadAll();

    assertThat(releases).size().isEqualTo(1);
    Release release = releases.get(0);

    List<Report> reports = reportRepository.loadAllForJob(release.getJobId());
    Map<String, String> reportContents = reports.stream().collect(Collectors.toMap(Report::getFilename, Report::getXbrl));

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(release.getContent());
    ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_16);
    try {
      for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
        assertThat(reportContents.containsKey(zipEntry.getName())).isTrue();
        String content = reportContents.get(zipEntry.getName());
        byte[] zipBytes = zipInputStream.readAllBytes();

        assertThat(zipBytes).isEqualTo(content.getBytes(StandardCharsets.UTF_16));
      }
    } finally {
      zipInputStream.close();
      byteArrayInputStream.close();
    }

  }

  private String getKeyName() {
    return new StringBuilder(KEYPREFIX).append(":").append(env).append(":").append(name).toString();
  }

}
