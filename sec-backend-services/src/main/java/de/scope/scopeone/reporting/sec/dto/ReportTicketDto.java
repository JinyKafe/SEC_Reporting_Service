package de.scope.scopeone.reporting.sec.dto;

import com.google.common.base.MoreObjects;
import de.scope.scopeone.reporting.sec.common.logging.MdcUtil;
import de.scope.scopeone.reporting.sec.dto.builder.ReportTicketDtoBuilder;
import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.ScopeReportJpaEntity;
import de.scope.scopeone.reporting.sec.type.StepStateType;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import gov.sec.ratings.Xbrl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ReportTicketDto {

  private String reportId;
  private String jobId;
  private String reportName;
  private XbrlType reportType;
  private StepType currentStep;
  private StepStateType currentStepState;
  private String errorMessage;
  // bellow are just temporal references to supporting objects. Do not serialise those as they can be always recreated
  private ScopeReportMetadata scopeReportMetadata;
  private ScopeReportJpaEntity scopeReportJpaEntity;
  private Xbrl xbrlEntity;
  private String xbrl;

  public void setReportId(String reportId) {
    MdcUtil.traceReportId(reportId);
    this.reportId = reportId;
  }

  public void setCurrentStep(StepType currentStep) {
    MdcUtil.traceTask(currentStep.name());
    this.currentStep = currentStep;
  }

  public void setCurrentStepState(StepStateType currentStepState) {
    log.trace("");
    MdcUtil.traceTaskStep(currentStepState.name());
    this.currentStepState = currentStepState;
  }

  public void setReportType(XbrlType reportType) {
    MdcUtil.traceReportType(reportType.name());
    this.reportType = reportType;
  }

  public static ReportTicketDtoBuilder builder() {
    return ReportTicketDtoBuilder.builder();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("reportId", reportId)
        .add("jobId", jobId)
        .add("reportName", reportName)
        .add("reportType", reportType)
        .add("currentStep", currentStep)
        .add("currentStepState", currentStepState)
        .add("errorMessage", errorMessage)
        .toString();
  }
}
