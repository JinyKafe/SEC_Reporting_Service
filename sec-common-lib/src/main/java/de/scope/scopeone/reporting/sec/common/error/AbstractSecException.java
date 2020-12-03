package de.scope.scopeone.reporting.sec.common.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSecException extends RuntimeException {

  private final SecErrorCode errorCode;

  public AbstractSecException(SecErrorCode errorCode, String message, Throwable cause) {
    super("Error " + errorCode.name() + ": " + message + " -> " + cause.getLocalizedMessage(), cause);
    this.errorCode = errorCode;
  }

  public AbstractSecException(SecErrorCode errorCode, String message) {
    super("Error " + errorCode.getErrorCode() + ": " + message);
    this.errorCode = errorCode;
  }

  public SecErrorCode getErrorCode() {
    return errorCode;
  }
}
