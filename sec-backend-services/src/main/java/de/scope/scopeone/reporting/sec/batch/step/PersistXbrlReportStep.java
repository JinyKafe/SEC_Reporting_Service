package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportState;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.service.XbrlRepositoryService;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersistXbrlReportStep extends AbstractReportingStep {

  private final XbrlRepositoryService xbrlRepositoryService;

  public PersistXbrlReportStep(XbrlRepositoryService xbrlRepositoryService) {
    this.xbrlRepositoryService = xbrlRepositoryService;
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    xbrlRepositoryService.saveReport(
        Report.builder()
            .id(reportTicketDto.getReportId())
            .jobId(reportTicketDto.getJobId())
            .state(ReportState.COMPLETED)
            .type(reportTicketDto.getReportType() == XbrlType.ISSUER ? ReportType.ISSUER : ReportType.OBLIGOR)
            .filename(reportTicketDto.getReportName())
            .xbrl(reportTicketDto.getXbrl())
            .build()
    );
  }

  @Override
  protected StepType getCurrentStep() {
    return StepType.PERSISTING;
  }

  @Override
  protected StepType getPrecedingStep() {
    return StepType.GENERATING;
  }

  @Override
  protected void doBefore(ReportTicketDto reportTicketDto) {
    if (reportTicketDto.getXbrl() == null) {
      throw new SecInternalError(SecErrorCode.BUG, "XBRL report must not be null");
    }
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    reportTicketDto.setXbrl(null);
  }
}
