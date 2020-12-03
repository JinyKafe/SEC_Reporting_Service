package de.scope.scopeone.reporting.sec.dto;

import de.scope.scopeone.reporting.sec.common.logging.MdcUtil;
import de.scope.scopeone.reporting.sec.dto.builder.JobTicketDtoBuilder;
import de.scope.scopeone.reporting.sec.type.JobStateType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class JobTicketDto {

  private String jobId;
  private JobStateType state;
  private String errorMessage;
  private LocalDateTime processingDate;
  private String originatorPerson;
  private List<ReportTicketDto> reportTickets;

  public void setJobId(String jobId) {
    MdcUtil.traceJobId(jobId);
    this.jobId = jobId;
  }

  public void setState(JobStateType state) {
    MdcUtil.traceJobState(state.name());
    this.state = state;
  }

  public String getJobId() {
    return jobId;
  }

  public JobStateType getState() {
    return state;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public LocalDateTime getProcessingDate() {
    return processingDate;
  }

  public void setProcessingDate(LocalDateTime processingDate) {
    this.processingDate = processingDate;
  }

  public String getOriginatorPerson() {
    return originatorPerson;
  }

  public void setOriginatorPerson(String originatorPerson) {
    this.originatorPerson = originatorPerson;
  }

  public List<ReportTicketDto> getReportTickets() {
    return reportTickets;
  }

  public void setReportTickets(@NotEmpty List<ReportTicketDto> reportTickets) {
    this.reportTickets = reportTickets;
  }

  public void addReportTicket(@NotNull ReportTicketDto reportTicket) {
    if (this.reportTickets == null) {
      this.reportTickets = new ArrayList<>();
    }
    this.reportTickets.add(reportTicket);
  }

  public static JobTicketDtoBuilder builder() {
    return JobTicketDtoBuilder.builder();
  }
}
