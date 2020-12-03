package de.scope.scopeone.reporting.sec.app.server.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import de.scope.scopeone.reporting.sec.app.BaseIntegrationTest;
import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.IssuerRepository;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.ObligorRepository;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Report;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportState;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.ReportType;
import de.scope.scopeone.reporting.sec.type.XbrlType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler;
import org.springframework.jdbc.support.xml.SqlXmlHandler;

public class ReleaseControllerTest extends BaseIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseControllerTest.class);

  @Autowired
  private ResourceLoader resourceLoader;


  private static final Map<String, String> TEST_ISSUER_NAMES = Map.of(
      "3641", "SCOPERATINGS-BANCO SANTANDER SA",
      "25410", "SCOPERATINGS-DEUTSCHE BANK AG",
      "492104", "SCOPERATINGS-Daimler AG",
      "451444", "SCOPERATINGS-Federal Republic of Germany",
      "451451", "SCOPERATINGS-French Republic",
      "451457", "SCOPERATINGS-Italian Republic",
      "459300", "SCOPERATINGS-Merck KGaA",
      "451465", "SCOPERATINGS-People's Republic of China",
      "556491", "SCOPERATINGS-Heta Funding Designated Activity Company",
      "461615", "SCOPERATINGS-Griffon Funding Ltd"
  );

  @Autowired
  JdbcTemplate xbrlRepositoryJdbcTemplate;

  @Autowired
  ObligorRepository obligorRepository;

  @Autowired
  IssuerRepository issuerRepository;

  @Test
  public void testProduce() {
    ResponseEntity<JsonNode> response = restTemplate.postForEntity("/api/v1/xbrl/produce", null, JsonNode.class);

    List<ScopeReportMetadata> allObligorRecords = obligorRepository.getAllObligorRecords();
    List<ScopeReportMetadata> allIssuerRecords = issuerRepository.getAllIssuerRecords();

    List<Report> reports = xbrlRepositoryJdbcTemplate.query("SELECT * FROM dbo.REPORT", (resultSet, i) -> toCompletedReport(resultSet));
    assertThat(reports).isNotEmpty();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNull();
  }

  private static Report toCompletedReport(ResultSet resultSet) throws SQLException {
    SqlXmlHandler sqlXmlHandler = new Jdbc4SqlXmlHandler();
    return Report.builder()
        .id(resultSet.getString("id"))
        .jobId(resultSet.getString("job_id"))
        .type(ReportType.valueOf(resultSet.getString("type")))
        .filename(resultSet.getString("filename"))
        .state(ReportState.valueOf(resultSet.getString("state")))
        .errorMessage(resultSet.getString("error_message"))
        .xbrl(sqlXmlHandler.getXmlAsString(resultSet, "xbrl"))
        .build();
  }

  private static Report toFailedReport(ResultSet resultSet) throws SQLException {
    return Report.builder()
        .id(resultSet.getString("id"))
        .jobId(resultSet.getString("job_id"))
        .type(ReportType.valueOf(resultSet.getString("type")))
        .filename(resultSet.getString("filename"))
        .state(ReportState.valueOf(resultSet.getString("state")))
        .errorMessage(resultSet.getString("error_message"))
        .xbrl(null)
        .build();
  }

  private String readResource(String fileName) throws IOException {
    return IOUtils.toString(resourceLoader.getResource("classpath:xbrl/" + fileName).getInputStream(), StandardCharsets.UTF_8);
  }

  private void verifySuccessfulReport(ScopeReportMetadata scopeReportMetadata) throws IOException {

    // OBLIGOR/ISSUER#ID in Scope database are used as Report#ID in XBRL database
    scopeReportMetadata.getIssuerName()
    String isuerId = scopeReportMetadata.getReportId();
    String issuerName = Optional.ofNullable(TEST_ISSUER_NAMES.get(scopeReportMetadata.getReportId()))
        .orElseThrow(() -> new RuntimeException("Unknown report ID#" + id));

    LOGGER.info("generated report file: " + reportFile);

    // TODO: file validation can be done much better using regexp
    assertThat(reportFile.getPath()).containsPattern(reportPrefix);
    assertThat(reportFile.getPath()).containsPattern(reportType);

    assertThat(reportFile).hasContent(readResource(expectedSecReportFile, "employees.dat"));

    // validate report
    Assertions.assertDoesNotThrow(() -> reportingValidationService.validateXbrlReportFile(reportFile));

    // cleanup
    FileUtils.forceDelete(reportFile);
  }
}