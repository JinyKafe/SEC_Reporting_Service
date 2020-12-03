package de.scope.scopeone.reporting.sec.module.xbrlrepository.service;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecIOException;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XbrlRepositoryServiceImpl implements XbrlRepositoryService {

  private final ReportRepository reportRepository;

  public XbrlRepositoryServiceImpl(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  @Override
  public void saveReport(Report report) {
    try {
      reportRepository.save(report);
    } catch (Exception e) {
      throw new SecIOException(SecErrorCode.SQLSERVER_ERROR, "Unable to persist xbrlreport", e);
    }
  }

  @Override
  public Report loadReport(String jobId, String reportId, ReportType reportType) {
    try {
      return reportRepository.loadByJobIdReportIdAndReportType(jobId, reportId, reportType);
    } catch (Exception e) {
      throw new SecIOException(SecErrorCode.SQLSERVER_ERROR, "Unable to load xbrl report", e);
    }
  }
}

