package de.scope.scopeone.reporting.sec.app.server.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

  private String error;
  private List<String> details;
}
