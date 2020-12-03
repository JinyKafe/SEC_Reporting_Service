package de.scope.scopeone.reporting.sec.module.xbrlrepository.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Release {

  private @NonNull String id;
  private @NonNull String jobId;
  private @NonNull String fileName;
  private @NonNull byte[] content;
  private @NonNull String state;
  private @NonNull LocalDate releaseDate;
}
