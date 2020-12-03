package de.scope.scopeone.reporting.sec.common.error;

public class SecInternalError extends AbstractSecException{

  public SecInternalError(SecErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public SecInternalError(SecErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
