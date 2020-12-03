package de.scope.scopeone.reporting.sec.app.server.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.JobStateType;
import de.scope.scopeone.reporting.sec.service.JobService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(JobController.class)
public class JobControllerTest {

  List<Job> jobs;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JobService jobService;

  @BeforeEach
  public void setUp() {
    jobs = Arrays.asList(Job.builder()
            .state(JobStateType.FINISHED)
            .executionDate(LocalDate.now())
            .id(UUID.randomUUID().toString()).build(),
        Job.builder()
            .state(JobStateType.FINISHED)
            .executionDate(LocalDate.now())
            .id(UUID.randomUUID().toString()).build());
  }

  @AfterEach
  public void tearDwon() {
    verifyNoMoreInteractions(jobService);
  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getJobs() throws Exception {

    when(jobService.getJobs(eq(0), eq(10))).thenReturn(jobs);

    this.mockMvc
        .perform(get("/api/v1/jobs").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)));

    verify(jobService).getJobs(eq(0), eq(10));
  }

  @Test
  @WithAnonymousUser
  public void getJobsWithNoCrendetials() throws Exception {

    this.mockMvc
        .perform(get("/api/v1/jobs").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().is4xxClientError());

  }


}
