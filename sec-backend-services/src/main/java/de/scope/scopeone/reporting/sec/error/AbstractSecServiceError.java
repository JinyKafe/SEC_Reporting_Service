package de.scope.scopeone.reporting.sec.error;

public abstract class AbstractSecServiceError extends RuntimeException {

  public AbstractSecServiceError() {
    super();
  }

  public AbstractSecServiceError(String message) {
    super(message);
  }

  public AbstractSecServiceError(String message, Throwable cause) {
    super(message, cause);
  }

  public AbstractSecServiceError(Throwable cause) {
    super(cause);
  }
}
