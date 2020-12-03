package de.scope.scopeone.reporting.sec.module.xbrlrepository.repository;

import static de.scope.scopeone.reporting.sec.common.error.SecErrorCode.SQLSERVER_ERROR;

import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.JobStateType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class JobRepository implements Dao<Job> {

  private final JdbcTemplate jdbcTemplate;

  public JobRepository(JdbcTemplate xbrlRepositoryJdbcTemplate) {
    this.jdbcTemplate = xbrlRepositoryJdbcTemplate;
  }

  private static Job entityToJob(ResultSet rs) throws SQLException {
    return Job.builder().id(rs.getString("id"))
        .state(JobStateType.valueOf(rs.getString("state").toUpperCase()))
        .executionDate(rs.getDate("execution_date").toLocalDate())
        .errorMessage(rs.getString("error_message"))
        .build();
  }

  @Override
  public void save(Job job) {

    try {
      jdbcTemplate.update("INSERT INTO dbo.JOB (id, state, error_message, execution_date) VALUES (?, ?, ?, ?);",
          job.getId(),
          job.getState().name(),
          job.getErrorMessage(),
          job.getExecutionDate()
      );
    } catch (DataAccessException e) {
      throw new SecInternalError(SQLSERVER_ERROR, "Unable to save Job#" + job.getId(), e);
    }
  }

  @Override
  public Job load(String id) {
    return jdbcTemplate.queryForObject("Select * FROM dbo.JOB where id=?;", new Object[]{id},
        (rs, rowNum) -> entityToJob(rs));

  }

  @Override
  public int delete(String id) {
    return jdbcTemplate.update("DELETE FROM dbo.JOB where id=?;", id);
  }

  @Override
  public int update(Job job) {
    try {
      return jdbcTemplate.update("UPDATE dbo.JOB set state=?,error_message=? where id=?;",
          job.getState().name(),
          job.getErrorMessage(),
          job.getId()
      );
    } catch (DataAccessException e) {
      throw new SecInternalError(SQLSERVER_ERROR, "Unable to update Job#" + job.getId(), e);
    }
  }

  public List<Job> loadByDateDesc(Integer offset, Integer limit) {
    return jdbcTemplate.query("Select * FROM dbo.JOB ORDER BY CAST(created_time AS DATETIME) DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;",
        new Object[]{offset, limit}, (rs, rowNum) -> entityToJob(rs)
    );
  }

}
