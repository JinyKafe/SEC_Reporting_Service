package de.scope.scopeone.reporting.sec.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportState;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReleaseRepository;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReportRepository;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@ExtendWith(MockitoExtension.class)
public class ZippingServiceTest {

  private final String jobId = UUID.randomUUID().toString();

  private final String issuerFileName = "test-data/Issuer-with-special-Characters.xml";

  private final String obligorFileName = "test-data/Obligor-with-special-Characters.xml";
  @Mock
  private ReportRepository reportRepository;
  @Mock
  private ReleaseRepository releaseRepository;
  @InjectMocks
  private ZippingServiceImpl zippingService;

  private List<Report> reports;

  private File issuerFile;

  private File obligorFile;

  @BeforeEach
  public void setUp() throws IOException {
    if (issuerFile == null && obligorFile == obligorFile) {
      ClassLoader classLoader = getClass().getClassLoader();
      issuerFile = new File(classLoader.getResource(issuerFileName).getFile());
      obligorFile = new File(classLoader.getResource(obligorFileName).getFile());
    }
    reports = new ArrayList<>();
    Report obligor = Report.builder()
        .id(UUID.randomUUID().toString())
        .jobId(jobId)
        .xbrl(FileUtils.readFileToString(obligorFile, StandardCharsets.UTF_8))
        .filename(obligorFile.getName())
        .state(ReportState.COMPLETED)
        .type(ReportType.OBLIGOR)
        .build();
    reports.add(obligor);
    Report issuer = Report.builder()
        .id(UUID.randomUUID().toString())
        .jobId(jobId)
        .xbrl(FileUtils.readFileToString(issuerFile, StandardCharsets.UTF_8))
        .filename(issuerFile.getName())
        .state(ReportState.COMPLETED)
        .type(ReportType.ISSUER)
        .build();
    reports.add(issuer);
  }

  @Test
  public void createZip() {

    String fileName = UUID.randomUUID().toString();
    when(reportRepository.loadAllForJob(eq(jobId))).thenReturn(reports);
    ArgumentCaptor<Release> releaseCaptor = ArgumentCaptor.forClass(Release.class);
    Mockito.doNothing().when(releaseRepository).save(any(Release.class));

    zippingService.createZip(jobId, fileName);

    verify(reportRepository).loadAllForJob(eq(jobId));
    verify(releaseRepository).save(releaseCaptor.capture());

    Release release = releaseCaptor.getValue();
    assertThat(release).isNotNull();
    assertThat(release.getJobId()).isEqualTo(jobId);
    assertThat(release.getFileName()).isEqualTo(fileName);
  }

  @Test
  public void createZipByteArray() throws IOException {

    byte[] byteArray = zippingService.createZipByteArray(reports);
    InputStream myInputStream = new ByteArrayInputStream(byteArray);
    ZipInputStream zip = new ZipInputStream(myInputStream, StandardCharsets.UTF_16);
    Map<String, String> reportContents = reports.stream().collect(Collectors.toMap(Report::getFilename, Report::getXbrl));

    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
      String content = new String(zip.readAllBytes(), StandardCharsets.UTF_16);
      assertThat(reportContents).containsKey(entry.getName())
          .extractingByKey(entry.getName()).isEqualTo(content);
    }
    zip.close();
    myInputStream.close();

  }

}
