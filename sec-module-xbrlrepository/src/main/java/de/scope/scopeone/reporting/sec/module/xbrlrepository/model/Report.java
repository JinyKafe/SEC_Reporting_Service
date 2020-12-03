package de.scope.scopeone.reporting.sec.module.xbrlrepository.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Report {

  private @NonNull String id;
  private @NonNull String jobId;
  private @NonNull ReportType type;
  private @NonNull ReportState state;
  private @NonNull String filename;
  private @NonNull String xbrl;
  private String errorMessage;
}
