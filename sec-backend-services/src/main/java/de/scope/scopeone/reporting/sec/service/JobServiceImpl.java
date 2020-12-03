package de.scope.scopeone.reporting.sec.service;

import de.scope.scopeone.reporting.sec.error.SecNotFoundException;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.JobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobServiceImpl implements JobService {

  private final JobRepository jobRepository;

  public JobServiceImpl(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Override
  public Job getLatest() {
    List<Job> jobs = jobRepository.loadByDateDesc(0, 1);
    if (jobs.isEmpty()) {
      log.warn("No job record found");
      throw new SecNotFoundException("No job record found");
    } else {
      return jobs.get(0);
    }
  }

  @Override
  public Job getById(String id) {
    try {
      log.warn(String.format("No release record found with id, %s", id));
      return jobRepository.load(id);
    } catch (EmptyResultDataAccessException e) {
      throw new SecNotFoundException(String.format("No job record found with id, %s", id),e);
    }
  }

  @Override
  public List<Job> getJobs(Integer offset, Integer limit) {
    return jobRepository.loadByDateDesc(offset, limit);
  }
}
