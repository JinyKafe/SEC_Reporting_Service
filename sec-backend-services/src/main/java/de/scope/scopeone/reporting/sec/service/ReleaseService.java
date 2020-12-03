package de.scope.scopeone.reporting.sec.service;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import java.util.List;

public interface ReleaseService {

  Release getLatestRelease();

  List<Release> getReleaseByJobId(String jobId);

  Release getReleaseById(String id);

  List<Release> getReleases(Integer offset, Integer limit);
}
