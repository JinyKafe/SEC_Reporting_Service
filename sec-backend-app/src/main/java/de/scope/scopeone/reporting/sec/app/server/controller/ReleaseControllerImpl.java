package de.scope.scopeone.reporting.sec.app.server.controller;

import de.scope.scopeone.reporting.sec.app.server.controller.dto.ReleaseDto;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import de.scope.scopeone.reporting.sec.service.ReleaseService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReleaseControllerImpl implements ReleaseController {

  private final ReleaseService releaseService;

  @Override
  public ResponseEntity<Resource> downloadLatestZipFile() {
    Release release = releaseService.getLatestRelease();
    Resource resource = new ByteArrayResource(release.getContent(), release.getFileName());
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @Override
  public ReleaseDto getReleaseById(String id) {
    Release release = releaseService.getReleaseById(id);
    return ReleaseDto.builder()
        .id(release.getId())
        .jobId(release.getJobId())
        .state(release.getState())
        .fileName(release.getFileName())
        .releaseDate(release.getReleaseDate())
        .build();
  }

  @Override
  public List<ReleaseDto> getReleasesByJobId(String jobId) {
    List<Release> releases = releaseService.getReleaseByJobId(jobId);
    return releases.stream()
        .map(release -> ReleaseDto.builder()
            .id(release.getId())
            .jobId(release.getJobId())
            .state(release.getState())
            .fileName(release.getFileName())
            .releaseDate(release.getReleaseDate())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<ReleaseDto> getReleases(Integer offset, Integer limit) {
    List<Release> releases = releaseService.getReleases(offset, limit);
    return releases.stream()
        .map(release -> ReleaseDto.builder()
            .id(release.getId())
            .jobId(release.getJobId())
            .state(release.getState())
            .fileName(release.getFileName())
            .releaseDate(release.getReleaseDate())
            .build())
        .collect(Collectors.toList());
  }
}