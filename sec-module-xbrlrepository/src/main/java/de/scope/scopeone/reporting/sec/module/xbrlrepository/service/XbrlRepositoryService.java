package de.scope.scopeone.reporting.sec.module.xbrlrepository.service;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;

public interface XbrlRepositoryService {

  void saveReport(Report t);

  Report loadReport(String jobId, String reportId, ReportType reportType);

}
