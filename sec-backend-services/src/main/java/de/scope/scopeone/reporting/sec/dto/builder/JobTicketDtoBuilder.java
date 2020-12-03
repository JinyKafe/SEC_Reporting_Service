package de.scope.scopeone.reporting.sec.dto.builder;

import de.scope.scopeone.reporting.sec.dto.JobTicketDto;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.type.JobStateType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public final class JobTicketDtoBuilder {

  private String jobId;
  private JobStateType currentState;
  private String errorMessage;
  private LocalDateTime processingDate;
  private String originatorPerson;
  private List<ReportTicketDto> reportTickets;

  private JobTicketDtoBuilder() {
  }

  public static JobTicketDtoBuilder builder() {
    return new JobTicketDtoBuilder();
  }

  public JobTicketDtoBuilder jobId(String jobId) {
    this.jobId = jobId;
    return this;
  }

  public JobTicketDtoBuilder processingState(JobStateType currentState) {
    this.currentState = currentState;
    return this;
  }

  public JobTicketDtoBuilder errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  public JobTicketDtoBuilder processingDate(LocalDateTime processingDate) {
    this.processingDate = processingDate;
    return this;
  }

  public JobTicketDtoBuilder originatorPerson(String originatorPerson) {
    this.originatorPerson = originatorPerson;
    return this;
  }

  public JobTicketDtoBuilder reportTickets(@NotEmpty List<ReportTicketDto> reportTickets) {
    this.reportTickets = reportTickets;
    return this;
  }

  public JobTicketDtoBuilder reportTicket(@NotNull ReportTicketDto reportTicketDto) {
    if (this.reportTickets == null) {
      this.reportTickets = new ArrayList<>();
    }
    this.reportTickets.add(reportTicketDto);
    return this;
  }

  public JobTicketDto build() {
    JobTicketDto jobTicketDto = new JobTicketDto();
    jobTicketDto.setJobId(jobId);
    jobTicketDto.setState(currentState);
    jobTicketDto.setErrorMessage(errorMessage);
    jobTicketDto.setProcessingDate(processingDate);
    jobTicketDto.setOriginatorPerson(originatorPerson);
    jobTicketDto.setReportTickets(reportTickets != null ? reportTickets : new ArrayList<>());
    return jobTicketDto;
  }
}
