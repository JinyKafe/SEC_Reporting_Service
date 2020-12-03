package de.scope.scopeone.reporting.sec.common.error;

public class SecBusinessException extends AbstractSecException {

  public SecBusinessException(SecErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public SecBusinessException(SecErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
