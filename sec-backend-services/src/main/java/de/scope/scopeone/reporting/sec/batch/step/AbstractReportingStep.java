package de.scope.scopeone.reporting.sec.batch.step;

import de.scope.scopeone.reporting.sec.common.error.AbstractSecException;
import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import de.scope.scopeone.reporting.sec.type.StepStateType;
import de.scope.scopeone.reporting.sec.type.StepType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractReportingStep implements ReportingStep {

  @Override
  public ReportTicketDto apply(ReportTicketDto reportTicketDto) {
    try {
      if (reportTicketDto.getCurrentStepState() != StepStateType.ERROR) {
        validateStateBefore(reportTicketDto);
        doBefore(reportTicketDto);

        reportTicketDto.setCurrentStep(getCurrentStep());
        reportTicketDto.setCurrentStepState(StepStateType.STARTED);
        perform(reportTicketDto);
        reportTicketDto.setCurrentStepState(StepStateType.FINISHED);

        validateStateAfter(reportTicketDto);
      }
    } catch (AbstractSecException e) {
      log.error(String.format("SEC Exception encountered for report %s in %s: %s", reportTicketDto.getReportName(), this.getClass().getSimpleName(),
          e.getLocalizedMessage()), e);
      reportTicketDto.setCurrentStepState(StepStateType.ERROR);
      reportTicketDto.setErrorMessage(e.getMessage());
    } catch (Exception e) {
      log.error(String.format("Unknown error encountered for report %s in %s: %s", reportTicketDto.getReportName(), this.getClass().getSimpleName(),
          e.getLocalizedMessage()), e);
      reportTicketDto.setCurrentStepState(StepStateType.ERROR);
      reportTicketDto.setErrorMessage("Unknown error: " + e.getLocalizedMessage());
    }

    return reportTicketDto;
  }

  protected abstract StepType getPrecedingStep();

  protected abstract StepType getCurrentStep();

  protected abstract void doBefore(ReportTicketDto reportTicketDto);

  protected abstract void perform(ReportTicketDto reportTicketDto);

  protected abstract void doAfter(ReportTicketDto reportTicketDto);

  private void validateStateBefore(ReportTicketDto reportTicketDto) {
    if (reportTicketDto.getCurrentStep() != getPrecedingStep()) {
      throw new SecInternalError(SecErrorCode.BUG,
          String.format("%s must precede to %s but it was %s", getPrecedingStep(), getCurrentStep(), reportTicketDto.getCurrentStep()));
    }
  }

  private void validateStateAfter(ReportTicketDto reportTicketDto) {
    if (reportTicketDto.getCurrentStep() != getCurrentStep() || reportTicketDto.getCurrentStepState() != StepStateType.FINISHED) {
      reportTicketDto.setCurrentStepState(StepStateType.ERROR);
      reportTicketDto.setErrorMessage(String.format("Ticket hangs in status %s/%s", reportTicketDto.getCurrentStep(), reportTicketDto.getCurrentStepState()));
    }
  }
}
