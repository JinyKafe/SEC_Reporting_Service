package de.scope.scopeone.reporting.sec.error;


public class SecServiceInternalError extends AbstractSecServiceError {

  public SecServiceInternalError() {
    super();
  }

  public SecServiceInternalError(String message) {
    super(message);
  }

  public SecServiceInternalError(String message, Throwable cause) {
    super(message, cause);
  }

  public SecServiceInternalError(Throwable cause) {
    super(cause);
  }
}
