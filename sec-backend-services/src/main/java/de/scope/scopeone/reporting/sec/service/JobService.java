package de.scope.scopeone.reporting.sec.service;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import java.util.List;

public interface JobService {

  Job getLatest();

  Job getById(String jobId);

  List<Job> getJobs(Integer offset, Integer limit);
}
