package de.scope.scopeone.reporting.sec.batch.job;

import de.scope.scopeone.reporting.sec.batch.step.GenerateXbrlReportStep;
import de.scope.scopeone.reporting.sec.batch.step.MapIssuerJpaEntityToXbrlEntityStep;
import de.scope.scopeone.reporting.sec.batch.step.PersistXbrlReportStep;
import de.scope.scopeone.reporting.sec.batch.step.ReadIssuerFromScopeStep;
import de.scope.scopeone.reporting.sec.batch.step.ReportingStep;
import de.scope.scopeone.reporting.sec.batch.step.ValidateProcessingResultStep;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IssuerReportGeneratorImpl extends AbstractJobExecutor {

  private final ReportingStep readIssuerFromScopeStep;
  private final ReportingStep mapIssuerJpaEntityToXbrlEntityStep;
  private final ReportingStep generateXbrlReportStep;
  private final ReportingStep persistXbrlReportStep;
  private final ReportingStep validateProcessingStep;

  public IssuerReportGeneratorImpl(ReadIssuerFromScopeStep readIssuerFromScopeStep,
                                   MapIssuerJpaEntityToXbrlEntityStep mapIssuerJpaEntityToXbrlEntityStep,
                                   GenerateXbrlReportStep generateXbrlReportStep,
                                   PersistXbrlReportStep persistXbrlReportStep,
                                   ValidateProcessingResultStep validateProcessingF) {
    this.readIssuerFromScopeStep = readIssuerFromScopeStep;
    this.mapIssuerJpaEntityToXbrlEntityStep = mapIssuerJpaEntityToXbrlEntityStep;
    this.generateXbrlReportStep = generateXbrlReportStep;
    this.persistXbrlReportStep = persistXbrlReportStep;
    this.validateProcessingStep = validateProcessingF;
  }

  @Override
  protected XbrlType reportTypeFilter() {
    return XbrlType.ISSUER;
  }

  @Override
  protected ReportTicketDto processTicket(ReportTicketDto issuerTicketDto) {
    return readIssuerFromScopeStep
        .andThen(mapIssuerJpaEntityToXbrlEntityStep)
        .andThen(generateXbrlReportStep)
        .andThen(persistXbrlReportStep)
        .andThen(validateProcessingStep)
        .apply(issuerTicketDto);
  }
}
