package de.scope.scopeone.reporting.sec.service;

import static de.scope.scopeone.reporting.sec.type.JobStateType.QUEUED;
import static java.util.stream.Collectors.toList;

import de.scope.scopeone.reporting.sec.batch.job.IssuerReportGeneratorImpl;
import de.scope.scopeone.reporting.sec.batch.job.ObligorReportGeneratorImpl;
import de.scope.scopeone.reporting.sec.batch.job.ReportingJob;
import de.scope.scopeone.reporting.sec.common.logging.MdcUtil;
import de.scope.scopeone.reporting.sec.dto.JobTicketDto;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.service.IssuerJpaService;
import de.scope.scopeone.reporting.sec.module.scoperepository.service.ObligorJpaService;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.JobStateType;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.JobRepository;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import de.scope.scopeone.reporting.sec.util.ReportingUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportingServiceImpl implements ReportingService {

  private final ObligorJpaService obligorJpaService;
  private final ReportingJob obligorReportGeneratorService;

  private final IssuerJpaService issuerJpaService;
  private final ReportingJob issuerReportGeneratorService;
  private final JobRepository jobRepository;

  public ReportingServiceImpl(ObligorJpaService obligorJpaService,
                              IssuerJpaService issuerJpaService,
                              ObligorReportGeneratorImpl obligorReportGenerator,
                              IssuerReportGeneratorImpl issuerReportGenerator,
                              JobRepository jobRepository) {
    this.obligorJpaService = obligorJpaService;
    this.obligorReportGeneratorService = obligorReportGenerator;
    this.issuerJpaService = issuerJpaService;
    this.issuerReportGeneratorService = issuerReportGenerator;
    this.jobRepository = jobRepository;
  }

  private static JobTicketDto prepareJobTicket(List<ScopeReportMetadata> reportDbRecords) {

    String jobId = UUID.randomUUID().toString();
    log.debug("Preparing job ticket#" + jobId);
    return JobTicketDto.builder()
        .jobId(jobId)
        .processingDate(LocalDateTime.now())
        // TO_DO replace with logged user
        .originatorPerson("TODO@loggedusermail.com")
        .processingState(QUEUED)
        .reportTickets(reportDbRecords.stream().map((ScopeReportMetadata reportDbRecord) ->
            ReportTicketDto.builder()
                .reportId(reportDbRecord.getReportId())
                .jobId(jobId)
                .reportName(
                    reportDbRecord.getReportType().equals(XbrlType.OBLIGOR.name()) ? ReportingUtils.getObligorReportFileName(reportDbRecord.getIssuerName())
                        : ReportingUtils.getIssuerReportFileName(reportDbRecord.getIssuerName())
                )
                .reportType(XbrlType.valueOf(reportDbRecord.getReportType()))
                .currentStep(StepType.QUEUED)
                .reportDbRecord(reportDbRecord)
                .build())
            .collect(toList()))
        .build();
  }

  @Override
  public JobTicketDto generateObligorReport(Integer obligorId) {

    log.debug("Going to find Oblior#" + obligorId);
    ScopeReportMetadata issuerDbRecord = obligorJpaService.getObligorRecordById(obligorId);

    log.debug("Going to prapare ticket for Obligor#" + obligorId);
    JobTicketDto jobTicket = prepareJobTicket(List.of(issuerDbRecord));

    log.info("Going to start Reporting Job for Obligor#" + obligorId);
    // TO_DO consider to use asynchronous call with CompletableFuture
    MdcUtil.resetAllMdcLoggings();
    return obligorReportGeneratorService.apply(jobTicket);
  }

  @Override
  public JobTicketDto generateIssuerReport(Integer issuerId) {

    log.debug("Going to find Issuer#" + issuerId);
    ScopeReportMetadata issuerDbRecord = issuerJpaService.getIssuerRecordById(issuerId);

    log.debug("Going to prapare ticket for Issuer#" + issuerId);
    JobTicketDto jobTicket = prepareJobTicket(List.of(issuerDbRecord));

    log.info("Going to start Reporting Job for Issuer#" + issuerId);
    // TO_DO consider to use asynchronous call with CompletableFuture
    MdcUtil.resetAllMdcLoggings();
    return issuerReportGeneratorService.apply(jobTicket);
  }

  @Override
  public JobTicketDto produce() {

    log.debug("Going to find all reports in Scope DB");
    List<ScopeReportMetadata> reportDbRecords = Stream
        .concat(issuerJpaService.getAllIssuerRecords().stream(), obligorJpaService.getAllObligorRecords().stream())
        .collect(toList());

    log.debug("Going to prapare ticket for all reports");
    JobTicketDto jobTicket = prepareJobTicket(reportDbRecords);

    jobRepository.save(Job.builder()
        .id(jobTicket.getJobId())
        .state(JobStateType.valueOf(jobTicket.getState().name().toUpperCase()))
        .errorMessage(jobTicket.getErrorMessage())
        .executionDate(LocalDate.now())
        .build());

    log.info("Going to start the Job#" + jobTicket.getJobId());
    // TO_DO consider to use asynchronous call with CompletableFuture
    obligorReportGeneratorService.apply(jobTicket);
    issuerReportGeneratorService.apply(jobTicket);

    jobRepository.update(Job.builder()
        .id(jobTicket.getJobId())
        .state(JobStateType.valueOf(jobTicket.getState().name().toUpperCase()))
        .errorMessage(jobTicket.getErrorMessage())
        .executionDate(LocalDate.now())
        .build());

    MdcUtil.resetAllMdcLoggings();
    return jobTicket;
  }
}
