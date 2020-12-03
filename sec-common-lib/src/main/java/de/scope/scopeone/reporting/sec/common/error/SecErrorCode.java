package de.scope.scopeone.reporting.sec.common.error;

public enum SecErrorCode {
  INVALID_DATA(1),
  OBJECT_NOT_FOUND(2),
  UNALLOWED_OPERATION(3),
  // INTERNAL ERRORS
  SQLSERVER_ERROR(98),
  UNKNOWN_ERROR(99),
  TODO(999),
  // remove TO_DO error code :) but it is useful to have is as reminder reference here during development
  BUG(98);

  private final int errorCode;

  SecErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
