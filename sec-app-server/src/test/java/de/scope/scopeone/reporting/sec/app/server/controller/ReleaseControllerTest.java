package de.scope.scopeone.reporting.sec.app.server.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.scope.scopeone.reporting.sec.error.SecNotFoundException;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Release;
import de.scope.scopeone.reporting.sec.service.ReleaseService;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@WebMvcTest(ReleaseController.class)
public class ReleaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReleaseService releaseService;

  private String[] fileNames = {"AAA", "BBB", "CCC"};

  private Release release;

  @BeforeEach
  public void setUp() throws IOException {
    release = Release.builder().id(UUID.randomUUID().toString())
        .jobId(UUID.randomUUID().toString())
        .state("SUCCESS")
        .releaseDate(LocalDate.now())
        .fileName("newzipFile")
        .content(createSampleZipFile())
        .build();
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(releaseService);
  }


  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getLatestFile() throws Exception {
    when(releaseService.getLatestRelease()).thenReturn(release);
    MvcResult result = this.mockMvc
        .perform(get("/api/v1/release/latest/zip").contentType("application/zip"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
    ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
    try {
      for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
        assertThat(zipEntry.getName()).isIn(fileNames);
        File clonedFile = createXmlFile(zipEntry.getName());
        assertThat(zipInputStream.readAllBytes()).isEqualTo(FileUtils.readFileToByteArray(clonedFile));
      }
    } finally {
      zipInputStream.close();
      byteArrayInputStream.close();
    }
    verify(releaseService).getLatestRelease();

  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getReleaseWithId() throws Exception {
    String id = release.getId();
    when(releaseService.getReleaseById(eq(id))).thenReturn(release);

    this.mockMvc
        .perform(get(String.format("/api/v1/release/%s", id)).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(release.getId()))
        .andExpect(jsonPath("$.jobId").value(release.getJobId()))
        .andExpect(jsonPath("$.fileName").value(release.getFileName()))
        .andExpect(jsonPath("$.state").value(release.getState()))
        .andExpect(jsonPath("$.releaseDate").value(release.getReleaseDate().toString()));

    verify(releaseService).getReleaseById(eq(id));

  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getReleaseWithNonExistingId() throws Exception {
    String id = UUID.randomUUID().toString();
    when(releaseService.getReleaseById(eq(id))).thenThrow(new SecNotFoundException(String.format("No release record found with id, %s", id)));

    this.mockMvc
        .perform(get(String.format("/api/v1/release/%s", id)).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.error").value("Record Not Found"));

    verify(releaseService).getReleaseById(eq(id));

  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getReleasesWithJobId() throws Exception {
    String jobId = release.getJobId();
    when(releaseService.getReleaseByJobId((eq(jobId)))).thenReturn(Arrays.asList(release));

    this.mockMvc
        .perform(get(String.format("/api/v1/release/with_job_id/%s", jobId)).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(release.getId()))
        .andExpect(jsonPath("$[0].jobId").value(release.getJobId()))
        .andExpect(jsonPath("$[0].fileName").value(release.getFileName()))
        .andExpect(jsonPath("$[0].state").value(release.getState()))
        .andExpect(jsonPath("$[0].releaseDate").value(release.getReleaseDate().toString()));

    verify(releaseService).getReleaseByJobId(eq(jobId));

  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getReleaseWithNonExistingJobId() throws Exception {
    String jobId = UUID.randomUUID().toString();
    when(releaseService.getReleaseByJobId(eq(jobId))).thenThrow(new SecNotFoundException(String.format("No release record found with id, %s", jobId)));

    this.mockMvc
        .perform(get(String.format("/api/v1/release/with_job_id/%s", jobId)).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.error").value("Record Not Found"));

    verify(releaseService).getReleaseByJobId(eq(jobId));

  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getReleasesWithDefaultOffsetAndLimit() throws Exception {

    when(releaseService.getReleases(eq(0), eq(10))).thenReturn(Arrays.asList(release));

    this.mockMvc.perform(get("/api/v1/release").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(release.getId()))
        .andExpect(jsonPath("$[0].jobId").value(release.getJobId()))
        .andExpect(jsonPath("$[0].fileName").value(release.getFileName()))
        .andExpect(jsonPath("$[0].state").value(release.getState()))
        .andExpect(jsonPath("$[0].releaseDate").value(release.getReleaseDate().toString()));

    verify(releaseService).getReleases(eq(0), eq(10));

  }

  @Test
  @WithMockUser(username = "${server.internal.http.auth.username}", password = "${server.internal.http.auth.password}", roles = "USER")
  public void getReleasesWithBeyondOffsetAndLimit() throws Exception {

    int offset = 10;
    when(releaseService.getReleases(eq(offset), eq(10))).thenReturn(Collections.emptyList());

    this.mockMvc.perform(get(String.format("/api/v1/release?offset=%d", offset)).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(0)));

    verify(releaseService).getReleases(eq(offset), eq(10));

  }

  private byte[] createSampleZipFile() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

    try {
      for (String name : fileNames) {
        ZipEntry zipEntry = new ZipEntry(name);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(FileUtils.readFileToString(createXmlFile(name), StandardCharsets.UTF_16).getBytes(StandardCharsets.UTF_16));
        zipOutputStream.closeEntry();
      }
    } finally {
      try {
        zipOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return byteArrayOutputStream.toByteArray();
  }

  private File createXmlFile(String fileName) throws IOException {
    File file = File.createTempFile(fileName, ".xml");

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_16))) {

      bw.write("<" + fileName + ">");
      bw.write(System.lineSeparator()); // new line

      bw.write(fileName);
      bw.write(System.lineSeparator());

      bw.write("</" + fileName + ">");

    }
    return file;
  }

}
