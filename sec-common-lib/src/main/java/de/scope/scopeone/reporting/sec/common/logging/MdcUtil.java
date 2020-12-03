package de.scope.scopeone.reporting.sec.common.logging;

import org.slf4j.MDC;

public final class MdcUtil {

  private static final String MDC_JOB_ID = "mdc-job-id";
  private static final String MDC_JOB_STATE = "mdc-job-state";
  private static final String MDC_REPORT_ID = "mdc-report-id";
  private static final String MDC_TASK = "mdc-task";
  private static final String MDC_TASK_STEP = "mdc-task-step";
  public static final String MDC_REPORT_TYPE = "mdc-report-type";

  private MdcUtil() {
  }

  public static void traceReportId(String requestId) {
    MDC.put(MDC_REPORT_ID, requestId);
  }

  public static void traceTask(String taskName) {
    MDC.put(MDC_TASK, taskName);
  }

  public static void traceTaskStep(String processingStep) {
    MDC.put(MDC_TASK_STEP, processingStep);
  }

  public static void traceJobId(String jobId) {
    MDC.put(MDC_JOB_ID, jobId);
  }

  public static void traceJobState(String jobState) {
    MDC.put(MDC_JOB_STATE, jobState);
  }

  public static void traceReportType(String reportType) {
    MDC.put(MDC_REPORT_TYPE, reportType);
  }

  public static void resetAllMdcLoggings() {
    MDC.remove(MDC_JOB_ID);
    MDC.remove(MDC_TASK);
    MDC.remove(MDC_TASK_STEP);
    MDC.remove(MDC_REPORT_ID);
    MDC.remove(MDC_REPORT_TYPE);
    MDC.remove(MDC_JOB_STATE);
  }
}
