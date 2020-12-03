package de.scope.scopeone.reporting.sec.app.server.conf;

import de.scope.scopeone.reporting.sec.app.server.controller.dto.ErrorResponse;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.error.SecNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SecExceptionHandler  extends ResponseEntityExceptionHandler {

  @ExceptionHandler(SecNotFoundException.class)
  public final ResponseEntity<Object> handleSecNotFoundException(SecNotFoundException ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse("Record Not Found", details);
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SecInternalError.class)
  public final ResponseEntity<Object> handleSecInternalError(SecInternalError ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse("Server Error", details);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
