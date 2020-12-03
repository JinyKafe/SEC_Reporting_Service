package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.scoperepository.service.ObligorJpaService;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReadObligorFromScopeStep extends AbstractReportingStep {

  private final ObligorJpaService obligorJpaService;

  public ReadObligorFromScopeStep(ObligorJpaService obligorJpaService) {
    this.obligorJpaService = obligorJpaService;
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    reportTicketDto.setScopeReportJpaEntity(obligorJpaService.findObligorById(Integer.valueOf(reportTicketDto.getReportId())));
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
    if (reportTicketDto.getReportType() != XbrlType.OBLIGOR) {
      throw new SecInternalError(SecErrorCode.BUG, "ReportTicket must be of type OBLIGOR");
    }

    if (reportTicketDto.getReportId() == null) {
      throw new SecInternalError(SecErrorCode.BUG, "ReportID must not be null");
    }
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    // DO nothing
  }
}
