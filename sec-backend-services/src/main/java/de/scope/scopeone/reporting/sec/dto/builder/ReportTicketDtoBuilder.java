package de.scope.scopeone.reporting.sec.dto.builder;

import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.type.StepStateType;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;

public final class ReportTicketDtoBuilder {

  private String reportId;
  private String jobId;
  private String reportName;
  private XbrlType reportType;
  private StepType currentStep;
  private StepStateType currentStepState = StepStateType.QUEUED;
  private ScopeReportMetadata scopeReportMetadata;

  private ReportTicketDtoBuilder() {
  }

  public static ReportTicketDtoBuilder builder() {
    return new ReportTicketDtoBuilder();
  }

  public ReportTicketDtoBuilder reportId(String reportId) {
    this.reportId = reportId;
    return this;
  }

  public ReportTicketDtoBuilder jobId(String jobId) {
    this.jobId = jobId;
    return this;
  }

  public ReportTicketDtoBuilder reportName(String reportName) {
    this.reportName = reportName;
    return this;
  }

  public ReportTicketDtoBuilder reportType(XbrlType reportType) {
    this.reportType = reportType;
    return this;
  }

  public ReportTicketDtoBuilder currentStep(StepType currentStep) {
    this.currentStep = currentStep;
    return this;
  }

  public ReportTicketDtoBuilder currentStepState(StepStateType currentStepState) {
    this.currentStepState = currentStepState;
    return this;
  }

  public ReportTicketDtoBuilder reportDbRecord(ScopeReportMetadata scopeReportMetadata) {
    this.scopeReportMetadata = scopeReportMetadata;
    return this;
  }

  public ReportTicketDto build() {
    ReportTicketDto reportTicketDto = new ReportTicketDto();
    reportTicketDto.setReportId(reportId);
    reportTicketDto.setJobId(jobId);
    reportTicketDto.setReportName(reportName);
    reportTicketDto.setReportType(reportType);
    reportTicketDto.setCurrentStep(currentStep);
    reportTicketDto.setCurrentStepState(currentStepState);
    reportTicketDto.setScopeReportMetadata(scopeReportMetadata);
    return reportTicketDto;
  }
}
