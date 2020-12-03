package de.scope.scopeone.reporting.sec.service;

import de.scope.scopeone.reporting.sec.error.SecNotFoundException;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReleaseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReleaseServiceImpl implements ReleaseService {

  private final ReleaseRepository releaseRepository;

  public ReleaseServiceImpl(ReleaseRepository releaseRepository) {
    this.releaseRepository = releaseRepository;
  }

  @Override
  public Release getLatestRelease() {
    List<Release> releases = releaseRepository.loadByReleaseDateDesc(0, 1);
    if (releases.isEmpty()) {
      log.warn("No release record found");
      throw new SecNotFoundException("No release record found");
    } else {
      return releases.get(0);
    }
  }

  @Override
  public List<Release> getReleaseByJobId(String jobId) {
    List<Release> releases = releaseRepository.loadByJobId(jobId);
    if (releases.isEmpty()) {
      log.warn(String.format("No release record found with job id, %s", jobId));
      throw new SecNotFoundException(String.format("No release record found with job id, %s", jobId));
    } else {
      return releases;
    }
  }

  @Override
  public Release getReleaseById(String id) {
    try {
      log.warn(String.format("No release record found with id, %s", id));
      return releaseRepository.load(id);
    } catch (EmptyResultDataAccessException e) {
      throw new SecNotFoundException(String.format("No release record found with id, %s", id),e);
    }
  }

  @Override
  public List<Release> getReleases(Integer offset, Integer limit) {
    return releaseRepository.loadByReleaseDateDesc(offset, limit);
  }

}
