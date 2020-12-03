package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.service.XbrlRepositoryService;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidateProcessingResultStep extends AbstractReportingStep {

  private final XbrlRepositoryService xbrlRepositoryService;

  public ValidateProcessingResultStep(XbrlRepositoryService xbrlRepositoryService) {
    this.xbrlRepositoryService = xbrlRepositoryService;
  }

  @Override
  protected StepType getPrecedingStep() {
    return StepType.PERSISTING;
  }

  @Override
  protected StepType getCurrentStep() {
    return StepType.VALIDATING;
  }

  @Override
  protected void doBefore(ReportTicketDto reportTicketDto) {
    // do nothing
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    ReportType reportType = reportTicketDto.getReportType() == XbrlType.OBLIGOR ? ReportType.OBLIGOR : ReportType.ISSUER;
    Report report = xbrlRepositoryService.loadReport(reportTicketDto.getJobId(), reportTicketDto.getReportId(),
        reportType);
    // TO_DO validate XML here
    log.warn("TO_DO JAXB validation of XBRL report not yet implemented  with id {}",report.getId());
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    // do nothing
  }
}
