package de.scope.scopeone.reporting.sec.module.xbrlrepository.repository;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.common.error.TODOReminderError;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportState;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler;
import org.springframework.jdbc.support.xml.SqlXmlHandler;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ReportRepository implements Dao<Report> {

  private final JdbcTemplate jdbcTemplate;

  public ReportRepository(JdbcTemplate xbrlRepositoryJdbcTemplate) {
    this.jdbcTemplate = xbrlRepositoryJdbcTemplate;
  }

  private static Report entityToReport(ResultSet resultSet) throws SQLException {
    SqlXmlHandler sqlXmlHandler = new Jdbc4SqlXmlHandler();
    return Report.builder()
        .id(resultSet.getString("id"))
        .jobId(resultSet.getString("job_id"))
        .type(ReportType.valueOf(resultSet.getString("type")))
        .filename(resultSet.getString("filename"))
        .state(ReportState.valueOf(resultSet.getString("state")))
        .errorMessage(resultSet.getString("error_message"))
        .xbrl(sqlXmlHandler.getXmlAsString(resultSet, "xbrl"))
        .build();
  }

  @Override
  public void save(Report report) {

    SqlXmlHandler sqlXmlHandler = new Jdbc4SqlXmlHandler();
    jdbcTemplate.update("INSERT INTO dbo.REPORT (id, job_id, type, filename, state, error_message, xbrl) VALUES (?, ?, ?, ?, ?, ?, ?);",
        report.getId(),
        report.getJobId(),
        report.getType().name(),
        report.getFilename(),
        report.getState().name(),
        report.getErrorMessage(),
        sqlXmlHandler.newSqlXmlValue(report.getXbrl())
    );
  }

  @Override
  public Report load(String id) {
    return jdbcTemplate.queryForObject("SELECT * FROM dbo.REPORT WHERE id =?;",
        new Object[]{id}, (resultSet, i) -> entityToReport(resultSet)
    );
  }

  @Override
  public int delete(String id) {
    return jdbcTemplate.update("DELETE FROM dbo.REPORT where id=?;", id);
  }

  public Report loadByJobIdReportIdAndReportType(String jobId, String reportId, ReportType reportType) {
    List<Report> reports = jdbcTemplate.query("SELECT * FROM dbo.REPORT WHERE id =? and job_id =? and type =?",
        new Object[]{reportId, jobId, reportType.name()}, (resultSet, i) -> entityToReport(resultSet)
    );

    switch (reports.size()) {
      case 1:
        return reports.get(0);
      case 0:
      default:
        throw new SecInternalError(SecErrorCode.BUG, "More than one report found but it must be unique");
    }
  }

  public int delete(String jobId, String reportId) {
    return jdbcTemplate.update("DELETE dbo.REPORT where job_id=? and id=?;",
        jobId, reportId
    );
  }

  @Override
  public int update(Report customer) {
    throw new TODOReminderError();
  }

  public List<Report> loadAllForJob(String jobId) {
    return jdbcTemplate.query("SELECT * FROM dbo.REPORT WHERE job_id =?",
        new Object[]{jobId}, (resultSet, i) -> entityToReport(resultSet)
    );
  }

}
