package de.scope.scopeone.reporting.sec.batch.job;

import de.scope.scopeone.reporting.sec.dto.JobTicketDto;
import java.util.function.Function;

public interface ReportingJob extends Function<JobTicketDto, JobTicketDto> {

  JobTicketDto apply(JobTicketDto jobTicketDto);
}
