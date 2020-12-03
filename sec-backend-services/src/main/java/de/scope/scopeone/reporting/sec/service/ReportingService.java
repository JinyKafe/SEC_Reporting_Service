package de.scope.scopeone.reporting.sec.service;

import de.scope.scopeone.reporting.sec.dto.JobTicketDto;

public interface ReportingService {


  JobTicketDto produce();

  JobTicketDto generateObligorReport(Integer obligorId);

  JobTicketDto generateIssuerReport(Integer issuerId);
}
