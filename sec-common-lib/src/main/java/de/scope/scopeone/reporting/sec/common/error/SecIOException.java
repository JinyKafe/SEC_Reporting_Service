package de.scope.scopeone.reporting.sec.common.error;

public class SecIOException extends AbstractSecException {

  public SecIOException(SecErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public SecIOException(SecErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
