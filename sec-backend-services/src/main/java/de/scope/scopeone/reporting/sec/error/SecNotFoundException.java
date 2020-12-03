package de.scope.scopeone.reporting.sec.error;


public class SecNotFoundException extends AbstractSecServiceError {

  public SecNotFoundException() {
    super();
  }

  public SecNotFoundException(String message) {
    super(message);
  }

  public SecNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SecNotFoundException(Throwable cause) {
    super(cause);
  }
}
