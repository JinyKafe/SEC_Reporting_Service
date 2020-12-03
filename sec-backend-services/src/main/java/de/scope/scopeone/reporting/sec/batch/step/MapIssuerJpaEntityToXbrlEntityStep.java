package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.service.IssuerMapperSerivce;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import de.scope.scopeone.reporting.sec.util.ReportingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MapIssuerJpaEntityToXbrlEntityStep extends AbstractReportingStep {

  private final IssuerMapperSerivce issuerMapperSerivce;

  public MapIssuerJpaEntityToXbrlEntityStep(IssuerMapperSerivce issuerMapperSerivce) {
    this.issuerMapperSerivce = issuerMapperSerivce;
  }

  @Override
  protected void perform(ReportTicketDto reportTicketDto) {
    Issuer issuerJpaEntity = (Issuer) reportTicketDto.getScopeReportJpaEntity();
    reportTicketDto.setXbrlEntity(issuerMapperSerivce.toXmlObject(issuerJpaEntity, ReportingUtils.getReportTime()));
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
    if (reportTicketDto.getReportType() != XbrlType.ISSUER) {
      throw new SecInternalError(SecErrorCode.BUG, "ReportTicket must be of type ISSUER");
    }

    if (reportTicketDto.getScopeReportJpaEntity() == null) {
      throw new SecInternalError(SecErrorCode.BUG, "JPA entity must not be null");
    }

    if (!(reportTicketDto.getScopeReportJpaEntity() instanceof Issuer)) {
      throw new SecInternalError(SecErrorCode.BUG, "JPA entity must be of type Issuer");
    }
  }

  @Override
  protected void doAfter(ReportTicketDto reportTicketDto) {
    reportTicketDto.setScopeReportJpaEntity(null);
  }
}
