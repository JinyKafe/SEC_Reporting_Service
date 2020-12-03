package de.scope.scopeone.reporting.sec.service;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import de.scope.scopeone.reporting.sec.error.SecNotFoundException;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.ReleaseRepository;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;


@ExtendWith(MockitoExtension.class)
public class ReleaseServiceTest {

  @Mock
  private ReleaseRepository releaseRepository;

  @InjectMocks
  private ReleaseServiceImpl releaseService;


  @Test
  public void getLatestReleaseWhenNoDataFound() {

    when(releaseRepository.loadByReleaseDateDesc(eq(0), eq(1)))
        .thenThrow(new SecNotFoundException("No release record found"));
    try {
      releaseService.getLatestRelease();
      fail("SecNotFoundException expected");
    } catch (SecNotFoundException e) {

    }

  }

  @Test
  public void getReleaseByIdWhenNoDataFound() {
    when(releaseRepository.load(any()))
        .thenThrow(new EmptyResultDataAccessException(0));
    try {
      releaseService.getReleaseById(UUID.randomUUID().toString());
      fail("SecNotFoundException expected");
    } catch (SecNotFoundException e) {

    }
  }

  @Test
  public void releaseByJobIdWhenEmptyListFound() {
    when(releaseRepository.loadByJobId(any()))
        .thenReturn(Collections.emptyList());
    try {
      releaseService.getReleaseByJobId(UUID.randomUUID().toString());
      fail("SecNotFoundException expected");
    } catch (SecNotFoundException e) {

    }
  }

}
