package de.scope.scopeone.reporting.sec.app.server.controller;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import de.scope.scopeone.reporting.sec.service.JobService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobControllerImpl implements JobController {

  private final JobService jobService;

  @Override
  public List<Job> getJobs(Integer offset, Integer limit) {
    return jobService.getJobs(offset,limit);
  }
}
