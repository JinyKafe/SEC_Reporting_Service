package de.scope.scopeone.reporting.sec.app.server.controller;

import de.scope.scopeone.reporting.sec.app.server.conf.ZipConfigProperties;
import de.scope.scopeone.reporting.sec.dto.JobTicketDto;
import de.scope.scopeone.reporting.sec.service.ReportingService;
import de.scope.scopeone.reporting.sec.service.ZippingService;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportingControllerImpl implements ReportingController {

  private final ReportingService reportingService;
  private final ZippingService zippingService;
  private final ZipConfigProperties zipConfigProperties;


  public ReportingControllerImpl(ReportingService reportingService,
                                 ZippingService zippingService,
                                 ZipConfigProperties zipConfigProperties) {
    this.reportingService = reportingService;
    this.zippingService = zippingService;
    this.zipConfigProperties = zipConfigProperties;
  }

  public String getNewFileName() {
    return String.format("%s-%s", zipConfigProperties.getNameprefix(), LocalDateTime.now().toString());
  }

  @Override
  public Void produceXblrFiles() {
    log.info("About to generate reports for all Obligors in DB");
    JobTicketDto jobTicketDto = reportingService.produce();
    // TO_DO remove this temporal logging and rather turn the jobTicketDto into REST API response
    log.info("Following reports have been created: ");
    log.info(jobTicketDto.toString());
    zippingService.createZip(jobTicketDto.getJobId(), getNewFileName());
    return null;
  }
}