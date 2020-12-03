package de.scope.scopeone.reporting.sec.service;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import de.scope.scopeone.reporting.sec.error.SecNotFoundException;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.repository.JobRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

  @Mock
  private JobRepository jobRepository;
  @InjectMocks
  private JobServiceImpl jobService;


  @Test
  public void getLatestWhenNoDataFound() {

    when(jobRepository.loadByDateDesc(eq(0), eq(1)))
        .thenThrow(new SecNotFoundException("No release record found"));
    try {
      jobService.getLatest();
      fail("SecNotFoundException expected");
    } catch (SecNotFoundException e) {

    }

  }

  @Test
  public void getByIdWhenNoDataFound() {
    when(jobRepository.load(any()))
        .thenThrow(new EmptyResultDataAccessException(0));
    try {
      jobService.getById(UUID.randomUUID().toString());
      fail("SecNotFoundException expected");
    } catch (SecNotFoundException e) {

    }
  }

}
