package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.xbrlgenerator.service.XbrlGeneratorService;
import de.scope.scopeone.reporting.sec.type.StepType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenerateXbrlReportStep extends AbstractReportingStep {

  private final XbrlGeneratorService xbrlGeneratorService;

  public GenerateXbrlReportStep(XbrlGeneratorService xbrlGeneratorService) {
    this.xbrlGeneratorService = xbrlGeneratorService;
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    reportTicketDto.setXbrl(xbrlGeneratorService.generateXbrlReport(reportTicketDto.getXbrlEntity()));
  }

  @Override
  protected StepType getCurrentStep() {
    return StepType.GENERATING;
  }

  @Override
  protected StepType getPrecedingStep() {
    return StepType.MAPPING;
  }

  @Override
  protected void doBefore(ReportTicketDto reportTicketDto) {
    if (reportTicketDto.getXbrlEntity() == null) {
      throw new SecInternalError(SecErrorCode.BUG, "XbrlEntity must not be null");
    }
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    reportTicketDto.setXbrlEntity(null);
  }
}
