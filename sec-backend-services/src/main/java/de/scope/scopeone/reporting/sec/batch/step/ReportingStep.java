package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import java.util.function.Function;

public interface ReportingStep extends Function<ReportTicketDto, ReportTicketDto> {

}
