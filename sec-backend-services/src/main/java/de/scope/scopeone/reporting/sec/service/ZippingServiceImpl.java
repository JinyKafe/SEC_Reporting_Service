package de.scope.scopeone.reporting.sec.service;

import de.scope.scopeone.reporting.sec.error.SecServiceInternalError;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReleaseRepository;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReportRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ZippingServiceImpl implements ZippingService {

  private final ReportRepository reportRepository;
  private final ReleaseRepository releaseRepository;

  public ZippingServiceImpl(ReportRepository reportRepository,
                            ReleaseRepository releaseRepository) {
    this.reportRepository = reportRepository;
    this.releaseRepository = releaseRepository;
  }

  @Override
  public void createZip(String jobId, String fileName) {
    List<Report> reports = reportRepository.loadAllForJob(jobId);
    byte[] zipContent;
    try {
      zipContent = createZipByteArray(reports);
      Release release = Release.builder()
          .id(UUID.randomUUID().toString())
          .jobId(jobId)
          .fileName(fileName)
          .content(zipContent)
          .state("FINISHED")
          .releaseDate(LocalDate.now())
          .build();
      releaseRepository.save(release);
      log.info("Report has been produced sucessfully for jobid {} with id {}", jobId, release.getId());
    } catch (IOException e) {
      log.error("Archive process has been failed", e);
      throw new SecServiceInternalError(e);
    }

  }


  protected byte[] createZipByteArray(List<Report> reports) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
      for (Report report : reports) {
        ZipEntry zipEntry = new ZipEntry(report.getFilename());
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(report.getXbrl().getBytes(StandardCharsets.UTF_16));
        zipOutputStream.closeEntry();
      }
    } catch (Exception e) {
      // TO_DO handle error
      log.error(e.getLocalizedMessage());
    }
    return byteArrayOutputStream.toByteArray();
  }
}
