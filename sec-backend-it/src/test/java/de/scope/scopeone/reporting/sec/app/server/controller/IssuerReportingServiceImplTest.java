package de.scope.scopeone.reporting.sec.app.server.controller;

import static org.assertj.core.api.Assertions.assertThat;

import de.scope.scopeone.reporting.sec.service.api.IssuerReportingService;
import de.scope.scopeone.reporting.sec.service.api.XbrlValidatorService;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/scope-sec-db-sample-data.sql"})
@Transactional
class IssuerReportingServiceImplTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(IssuerReportingServiceImplTest.class);

  @Value("classpath:xbrl/SCOPERATINGS-DEUTSCHE BANK AG-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_deutscheBank;
  @Value("classpath:xbrl/SCOPERATINGS-BANCO SANTANDER SA-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_bankoSantanderSa;
  @Value("classpath:xbrl/SCOPERATINGS-Federal Republic of Germany-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_federalRepublicOfGermany;
  @Value("classpath:xbrl/SCOPERATINGS-French Republic-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_frenchRepublic;
  @Value("classpath:xbrl/SCOPERATINGS-Italian Republic-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_italianRepublic;
  @Value("classpath:xbrl/SCOPERATINGS-Merck KGaA-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_mercKgaa;
  @Value("classpath:xbrl/SCOPERATINGS-People's Republic of China-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_peoplesRepublicOfChina;
  @Value("classpath:xbrl/SCOPERATINGS-Heta Funding Designated Activity Company-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_hetaFunding;
  @Value("classpath:xbrl/SCOPERATINGS-Griffon Funding Ltd-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_griffon;
  @Value("classpath:xbrl/SCOPERATINGS-Daimler AG-yyyy-MM-dd-ISSUER.xml")
  private Resource issuerReport_daimler;

  @SpyBean
  private IssuerReportingService issuerReportingService;

  @Autowired
  private XbrlValidatorService reportingValidationService;

  @Test
  void contextLoads() {
    assertThat(issuerReportingService).isNotNull();
  }

  @Test
  void createIssuerReport_BancoSantanderSa() throws IOException {
    verifyReportFile(3641, "SCOPERATINGS-BANCO SANTANDER SA-", "ISSUER", issuerReport_bankoSantanderSa);
  }

  @Test
  void createIssuerReport_DeutscheBank() throws IOException {
    verifyReportFile(25410, "SCOPERATINGS-DEUTSCHE BANK AG-", "ISSUER", issuerReport_deutscheBank);
  }

  @Test
  void createIssuerReport_Daimler() throws IOException, InterruptedException {
    verifyReportFile(492104, "SCOPERATINGS-Daimler AG-", "ISSUER", issuerReport_daimler);
  }

  @Test
  void createIssuerReport_FederalRepublicOfGermany() throws IOException {
    // SCOPERATINGS-Federal Republic of Germany-yyyy-MM-dd-ISSUER.xml
    verifyReportFile(451444, "SCOPERATINGS-Federal Republic of Germany-", "ISSUER", issuerReport_federalRepublicOfGermany);
  }

  @Test
  void createIssuerReport_FrenchRepublic() throws IOException {
    // SCOPERATINGS-French Republic-yyyy-MM-dd-ISSUER.xml
    verifyReportFile(451451, "SCOPERATINGS-French Republic-", "ISSUER", issuerReport_frenchRepublic);
  }

  @Test
  void createIssuerReport_ItalianRepublic() throws IOException {
    // SCOPERATINGS-Italian Republic-yyyy-MM-dd-ISSUER.xml
    verifyReportFile(451457, "SCOPERATINGS-Italian Republic-", "ISSUER", issuerReport_italianRepublic);
  }

  @Test
  void createIssuerReport_MercKGaA() throws IOException {
    // SCOPERATINGS-Merck KGaA-yyyy-MM-dd-ISSUER.xml
    verifyReportFile(459300, "SCOPERATINGS-Merck KGaA-", "ISSUER", issuerReport_mercKgaa);
  }

  @Test
  void createIssuerReport_PoplesRepublicOfChina() throws IOException {
    // SCOPERATINGS-People's Republic of China-yyyy-MM-dd-ISSUER.xml
    verifyReportFile(451465, "SCOPERATINGS-People's Republic of China-", "ISSUER", issuerReport_peoplesRepublicOfChina);
  }

  @Test
  void createIssuerReport_HetaFunding() throws IOException {
    verifyReportFile(556491, "SCOPERATINGS-Heta Funding Designated Activity Company-", "ISSUER", issuerReport_hetaFunding);
  }

  @Test
  void createIssuerReport_Griffon() throws IOException {
    verifyReportFile(461615, "SCOPERATINGS-Griffon Funding Ltd-", "ISSUER", issuerReport_griffon);
  }

  @Test
  void nameWithSpecialCharacters() {
    String fileName = "Deutche \\ Bank / //\\Universe \\ One";
    String expectedName = "Deutche  Bank  Universe  One";
    assertThat(fileName.replaceAll("[\\/\\\\]", "")).isEqualTo(expectedName);
  }

  private static String readResource(Resource resource) throws IOException {
    return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
  }

  private void verifyReportFile(int id, String reportPrefix, String reportType, Resource expectedSecReportFile) throws IOException {

    issuerReportingService.setReportingTime(LocalDateTime.of(2020, Month.JUNE, 5, 19, 30, 40));

    File reportFile = issuerReportingService.createIssuerReport(id, "./");
    LOGGER.info("generated report file: " + reportFile);

    // TODO: file validation can be done much better using regexp
    assertThat(reportFile.getPath()).containsPattern(reportPrefix);
    assertThat(reportFile.getPath()).containsPattern(reportType);

    assertThat(reportFile).hasContent(readResource(expectedSecReportFile));

    // validate report
    Assertions.assertDoesNotThrow(() -> reportingValidationService.validateXbrlReportFile(reportFile));

    // cleanup
    FileUtils.forceDelete(reportFile);
  }
}