package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import de.scope.scopeone.reporting.sec.module.scoperepository.service.IssuerJpaService;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReadIssuerFromScopeStep extends AbstractReportingStep {

  private final IssuerJpaService issuerJpaService;

  public ReadIssuerFromScopeStep(IssuerJpaService issuerJpaService) {
    this.issuerJpaService = issuerJpaService;
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    Issuer issuer = issuerJpaService.findIssuerById(Integer.valueOf(reportTicketDto.getReportId()));
    reportTicketDto.setScopeReportJpaEntity(issuer);
  }

  @Override
  protected StepType getPrecedingStep() {
    return StepType.QUEUED;
  }

  @Override
  protected StepType getCurrentStep() {
    return StepType.READING;
  }

  @Override
  protected void doBefore(ReportTicketDto reportTicketDto) {
    if (reportTicketDto.getReportType() != XbrlType.ISSUER) {
      throw new SecInternalError(SecErrorCode.BUG, "ReportTicket must be of type ISSUER");
    }

    if (reportTicketDto.getReportId() == null) {
      throw new SecInternalError(SecErrorCode.BUG, "ReportID must not be null");
    }
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    // do nothing
  }
}
