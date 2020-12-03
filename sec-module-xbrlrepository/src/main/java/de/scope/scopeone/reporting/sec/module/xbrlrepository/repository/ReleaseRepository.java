package de.scope.scopeone.reporting.sec.module.xbrlrepository.repository;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ReleaseRepository implements Dao<Release> {

  private final JdbcTemplate jdbcTemplate;

  public ReleaseRepository(JdbcTemplate xbrlRepositoryJdbcTemplate) {
    this.jdbcTemplate = xbrlRepositoryJdbcTemplate;
  }

  private static Release entityToRelease(ResultSet rs) throws SQLException {
    return Release.builder().id(rs.getString("id"))
        .jobId(rs.getString("job_id"))
        .fileName(rs.getString("archive_file_name"))
        .content(rs.getBytes("archive_file"))
        .state(rs.getString("state"))
        .releaseDate(rs.getDate("release_date").toLocalDate())
        .build();
  }

  @Override
  public void save(Release release) {
    jdbcTemplate.update("INSERT INTO dbo.RELEASE (id, job_id, archive_file_name, archive_file, state, release_date) VALUES (?, ?, ?, ?, ?, ?);",
        release.getId(),
        release.getJobId(),
        release.getFileName(),
        release.getContent(),
        release.getState(),
        release.getReleaseDate()
    );
  }

  @Override
  public int update(Release release) {
    return jdbcTemplate.update("UPDATE dbo.RELEASE set archive_file_name=?,archive_file=? where id=?;",
        release.getFileName(),
        release.getContent(),
        release.getId()
    );
  }

  @Override
  public int delete(String id) {
    return jdbcTemplate.update("DELETE FROM dbo.RELEASE where id=?;",
        id
    );
  }

  @Override
  public Release load(String id) {
    return jdbcTemplate.queryForObject("Select * FROM dbo.RELEASE where id=?;", new Object[]{id}, (rs, rowNum) ->
        entityToRelease(rs)
    );
  }

  public List<Release> loadByJobId(String jobId) {
    return jdbcTemplate.query("Select * FROM dbo.RELEASE where job_id=?;", new Object[]{jobId}, (rs, rowNum) ->
        entityToRelease(rs)
    );
  }

  public List<Release> loadAll() {
    return jdbcTemplate.query("Select * FROM dbo.RELEASE", (rs, rowNum) ->
        entityToRelease(rs)
    );
  }

  public List<Release> loadByReleaseDateDesc(Integer offset, Integer limit) {
    return jdbcTemplate.query("Select * FROM dbo.RELEASE ORDER BY CAST (created_time AS DATETIME) DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;",
        new Object[]{offset, limit}, (rs, rowNum) -> entityToRelease(rs)
    );
  }


}
