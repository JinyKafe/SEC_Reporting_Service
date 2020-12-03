package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.service.ObligorMapperService;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import de.scope.scopeone.reporting.sec.util.ReportingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MapObligorJpaEntityToXbrlEntityStep extends AbstractReportingStep {

  private final ObligorMapperService obligorMapperService;

  public MapObligorJpaEntityToXbrlEntityStep(ObligorMapperService obligorMapperService) {
    this.obligorMapperService = obligorMapperService;
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    Obligor obligorJpaEntity = (Obligor) reportTicketDto.getScopeReportJpaEntity();
    reportTicketDto.setXbrlEntity(obligorMapperService.toXmlObject(obligorJpaEntity, ReportingUtils.getReportTime()));
  }

  @Override
  protected StepType getPrecedingStep() {
    return StepType.READING;
  }

  @Override
  protected StepType getCurrentStep() {
    return StepType.MAPPING;
  }

  @Override
  protected void doBefore(ReportTicketDto reportTicketDto) {
    if (reportTicketDto.getReportType() != XbrlType.OBLIGOR) {
      throw new SecInternalError(SecErrorCode.BUG, "ReportTicket must be of type OBLIGOR");
    }

    if (reportTicketDto.getScopeReportJpaEntity() == null) {
      throw new SecInternalError(SecErrorCode.BUG, "JPA entity must not be null");
    }

    if (!(reportTicketDto.getScopeReportJpaEntity() instanceof Obligor)) {
      throw new SecInternalError(SecErrorCode.BUG, "JPA entity must be of type Obligor");
    }
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    reportTicketDto.setScopeReportJpaEntity(null);
  }
}
