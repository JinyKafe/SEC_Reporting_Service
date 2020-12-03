package de.scope.scopeone.reporting.sec.module.scoperepository.dto;

import java.time.LocalDateTime;

public interface ScopeReportMetadata {

  String getReportId();

  // TO_DO: use XbrlType instead of String
  String getReportType();

  String getIssuerName();

  String getScopeId();

  String getSecCategory();

  String getScopeCategory();

  LocalDateTime getCreatedDate();

}
