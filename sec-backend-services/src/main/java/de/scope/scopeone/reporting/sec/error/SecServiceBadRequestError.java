package de.scope.scopeone.reporting.sec.error;


public class SecServiceBadRequestError extends AbstractSecServiceError {

  public SecServiceBadRequestError() {
    super();
  }

  public SecServiceBadRequestError(String message) {
    super(message);
  }

  public SecServiceBadRequestError(String message, Throwable cause) {
    super(message, cause);
  }

  public SecServiceBadRequestError(Throwable cause) {
    super(cause);
  }
}
