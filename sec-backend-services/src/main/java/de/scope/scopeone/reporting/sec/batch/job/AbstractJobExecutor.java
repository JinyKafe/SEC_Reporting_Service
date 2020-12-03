package de.scope.scopeone.reporting.sec.batch.job;

import de.scope.scopeone.reporting.sec.dto.JobTicketDto;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.type.JobStateType;
import de.scope.scopeone.reporting.sec.type.StepStateType;
import de.scope.scopeone.reporting.sec.type.StepType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

// TO_DO redesign the whole executor desight. Use asynchronous CompletableFuture with TaskExecutors instead of synchronous Function calls
// also improve the readability. The code is bit too complicated now. But working....
@Slf4j
public abstract class AbstractJobExecutor implements ReportingJob {

  @Override
  public JobTicketDto apply(JobTicketDto jobTicketDto) {

    jobTicketDto.setState(JobStateType.STARTED);
    log.info("Job {} started...", jobTicketDto.getJobId());
    try {
      String errorMessages = jobTicketDto.getReportTickets().stream()
          .filter(tick -> tick.getReportType() == reportTypeFilter())
          // TO_DO make ready for parallel processing
          //.parallel()
          .map(this::processTicket)
          .filter(isError())
          .map(tick -> String.format("%s / %s / %s %n", tick.getReportType(), tick.getReportId(), tick.getErrorMessage()))
          .collect(Collectors.joining());

      if (StringUtils.isBlank(errorMessages)) {
        jobTicketDto.setState(JobStateType.FINISHED);
      } else {
        jobTicketDto.setState(JobStateType.ERROR);
        jobTicketDto.setErrorMessage(errorMessages);
      }
    } catch (Exception e) {
      // This block is just for case. You should never get here as all the buisiness exceptions are handled in each ReportingStep
      log.error("Unknown System Error", e);
      jobTicketDto.setErrorMessage(jobTicketDto.getErrorMessage() + "\nUnknown System Error: " + e.getLocalizedMessage());
      jobTicketDto.setState(JobStateType.ERROR);
    } finally {
      log.info("Job {} finished with state {}...", jobTicketDto.getJobId(), jobTicketDto.getState());
    }
    return jobTicketDto;
  }

  private static Predicate<ReportTicketDto> isError() {
    return reportTicketDto -> reportTicketDto.getCurrentStep() != StepType.VALIDATING ||
        reportTicketDto.getCurrentStepState() != StepStateType.FINISHED;
  }

  protected abstract XbrlType reportTypeFilter();

  protected abstract ReportTicketDto processTicket(ReportTicketDto reportTicketDto);
}
