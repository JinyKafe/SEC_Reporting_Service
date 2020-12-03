package de.scope.scopeone.reporting.sec.util;

import de.scope.scopeone.reporting.sec.dto.ReportTicketDto;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SuppressWarnings("squid:S1694" /* An abstract class should have both abstract and concrete methods */)
public final class ReportingUtils {

  private static final String OBLIGOR_REPORT_FILE_PATTERN = "SCOPERATINGS-%s-%tF-OBLIGOR.xml";
  private static final String ISSUER_REPORT_FILE_PATTERN = "SCOPERATINGS-%s-%tF-ISSUER.xml";


  private ReportingUtils() {
  }

  public static String getIssuerReportFileName(String issuerName) {
    String refinedName = issuerName.replaceAll("[\\/\\\\]", "");
    return String.format(ISSUER_REPORT_FILE_PATTERN, refinedName, new Date());
  }

  public static String getObligorReportFileName(String obligorName) {
    String refinedName = obligorName.replaceAll("[\\/\\\\]", "");
    return String.format(OBLIGOR_REPORT_FILE_PATTERN, refinedName, new Date());
  }

  public static LocalDateTime getReportTime() {
    return LocalDateTime.now();
  }

  public static String joinReportIdsAsString(List<ReportTicketDto> failedReports) {
    StringBuilder sb = new StringBuilder();
    failedReports.stream().map(ReportTicketDto::getReportId).forEach(reportId -> sb.append(reportId).append(", "));
    return sb.toString();
  }
}
