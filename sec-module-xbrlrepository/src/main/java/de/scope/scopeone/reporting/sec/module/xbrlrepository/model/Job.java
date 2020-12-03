package de.scope.scopeone.reporting.sec.module.xbrlrepository.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Job {

  private @NonNull String id;
  private @NonNull JobStateType state;
  private String errorMessage;
  private @NonNull LocalDate executionDate;
  private Timestamp createdTime;

}
