package de.scope.scopeone.reporting.sec.app.server.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(ReportingController.class)
public class ProcessTriggerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReportingController releaseController;

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void successfulTrigger() throws Exception {
    doNothing().when(releaseController).produceXblrFiles();
    this.mockMvc.perform(post("/api/v1/xbrl/produce"))
        .andDo(print()).andExpect(status().isOk());
    verify(releaseController, times(1)).produceXblrFiles();
  }

  @Test
  @WithAnonymousUser()
  public void unauthorizedRequest() throws Exception {
    doNothing().when(releaseController).produceXblrFiles();
    this.mockMvc.perform(post("/api/v1/xbrl/produce"))
        .andDo(print()).andExpect(status().is4xxClientError());
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(releaseController);
  }
}
