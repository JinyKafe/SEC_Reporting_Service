package de.scope.scopeone.reporting.sec.common.error;

// TO_DO remove this error, after there is no more reference to it in the code (means after all to_do's are solved)
public class TODOReminderError extends AbstractSecException {

  public TODOReminderError() {
    super(SecErrorCode.TODO, "Finish me");
  }

  public TODOReminderError(String todoMessage) {
    super(SecErrorCode.TODO, todoMessage);
  }

  public TODOReminderError(SecErrorCode code, String todoMessage) {
    super(code, todoMessage);
  }

  public TODOReminderError(SecErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
