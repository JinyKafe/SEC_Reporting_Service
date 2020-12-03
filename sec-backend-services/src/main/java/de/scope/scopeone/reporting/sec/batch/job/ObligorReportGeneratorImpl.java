package de.scope.scopeone.reporting.sec.batch.job;

import de.scope.scopeone.reporting.sec.batch.step.GenerateXbrlReportStep;
import de.scope.scopeone.reporting.sec.batch.step.MapObligorJpaEntityToXbrlEntityStep;
import de.scope.scopeone.reporting.sec.batch.step.PersistXbrlReportStep;
import de.scope.scopeone.reporting.sec.batch.step.ReadObligorFromScopeStep;
import de.scope.scopeone.reporting.sec.batch.step.ReportingStep;
import de.scope.scopeone.reporting.sec.batch.step.ValidateProcessingResultStep;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObligorReportGeneratorImpl extends AbstractJobExecutor {

  private final ReportingStep readObligorFromScopeStep;
  private final ReportingStep mapObligorJpaEntityToXbrlEntityStep;
  private final ReportingStep generateXbrlReportStep;
  private final ReportingStep persistXbrlReportStep;
  private final ReportingStep validateProcessingStep;

  public ObligorReportGeneratorImpl(ReadObligorFromScopeStep readObligorFromScopeStep,
                                    MapObligorJpaEntityToXbrlEntityStep mapObligorJpaEntityToXbrlEntityStep,
                                    GenerateXbrlReportStep generateXbrlReportStep,
                                    PersistXbrlReportStep persistXbrlReportStep,
                                    ValidateProcessingResultStep validateProcessingF) {
    this.readObligorFromScopeStep = readObligorFromScopeStep;
    this.mapObligorJpaEntityToXbrlEntityStep = mapObligorJpaEntityToXbrlEntityStep;
    this.generateXbrlReportStep = generateXbrlReportStep;
    this.persistXbrlReportStep = persistXbrlReportStep;
    this.validateProcessingStep = validateProcessingF;
  }

  @Override
  protected XbrlType reportTypeFilter() {
    return XbrlType.OBLIGOR;
  }

  @Override
  protected ReportTicketDto processTicket(ReportTicketDto issuerTicketDto) {
    return readObligorFromScopeStep
        .andThen(mapObligorJpaEntityToXbrlEntityStep)
        .andThen(generateXbrlReportStep)
        .andThen(persistXbrlReportStep)
        .andThen(validateProcessingStep)
        .apply(issuerTicketDto);
  }
}
