package de.scope.scopeone.reporting.sec.app.server.controller.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ReleaseDto {

  private @NonNull String id;
  private @NonNull String jobId;
  private @NonNull String fileName;
  private @NonNull String state;
  private @NonNull LocalDate releaseDate;
}
