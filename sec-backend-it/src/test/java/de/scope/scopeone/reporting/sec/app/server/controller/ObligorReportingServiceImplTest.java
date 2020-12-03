package de.scope.scopeone.reporting.sec.app.server.controller;

import static org.assertj.core.api.Assertions.assertThat;

import de.scope.scopeone.reporting.sec.service.api.ObligorReportingService;
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
class ObligorReportingServiceImplTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ObligorReportingServiceImplTest.class);

  @Value("classpath:xbrl/SCOPERATINGS-DEUTSCHE BANK AG-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligorReport_deutscheBank;
  @Value("classpath:xbrl/SCOPERATINGS-Daimler AG-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligoReport_daimler;
  @Value("classpath:xbrl/SCOPERATINGS-Federal Republic of Germany-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligorReport_federalRepublicOfGermany;
  @Value("classpath:xbrl/SCOPERATINGS-French Republic-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligorReport_frenchRepublic;
  @Value("classpath:xbrl/SCOPERATINGS-Italian Republic-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligorReport_italianRepublic;
  @Value("classpath:xbrl/SCOPERATINGS-Merck KGaA-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligorReport_mercKgaa;
  @Value("classpath:xbrl/SCOPERATINGS-People's Republic of China-yyyy-MM-dd-OBLIGOR.xml")
  private Resource obligorReport_peopleRepublicOfChina;

  @SpyBean
  private ObligorReportingService obligorReportingService;

  @Autowired
  private XbrlValidatorService reportingValidationService;

  @Test
  void contextLoads() {
    assertThat(obligorReportingService).isNotNull();
  }

  @Test
  void createObligorReport_DeutscheBank() throws IOException {
    verifyReportFile(25410, "SCOPERATINGS-DEUTSCHE BANK AG-", "OBLIGOR", obligorReport_deutscheBank);
  }

  @Test
  void createObligorReport_Daimler() throws IOException {
    // SCOPERATINGS-Daimler AG-yyyy-MM-dd-OBLIGOR.xml
    verifyReportFile(492104, "SCOPERATINGS-Daimler AG-", "OBLIGOR", obligoReport_daimler);
  }

  @Test
  void createObligorReport_FederalRepublicOfGermany() throws IOException {
    // SCOPERATINGS-Federal Republic of Germany-yyyy-MM-dd-OBLIGOR.xml
    verifyReportFile(451444, "SCOPERATINGS-Federal Republic of Germany-", "OBLIGOR", obligorReport_federalRepublicOfGermany);
  }

  @Test
  void createObligorReport_FrenchRepublic() throws IOException {
    // SCOPERATINGS-French Republic-yyyy-MM-dd-OBLIGOR.xml
    verifyReportFile(451451, "SCOPERATINGS-French Republic-", "OBLIGOR", obligorReport_frenchRepublic);
  }

  @Test
  void createObligorReport_ItalianRepublic() throws IOException {
    // SCOPERATINGS-Italian Republic-yyyy-MM-dd-OBLIGOR.xml
    verifyReportFile(451457, "SCOPERATINGS-Italian Republic-", "OBLIGOR", obligorReport_italianRepublic);
  }

  @Test
  void createObligorReport_MercKGaA() throws IOException {
    // SCOPERATINGS-Merck KGaA-yyyy-MM-dd-OBLIGOR.xml
    verifyReportFile(459300, "SCOPERATINGS-Merck KGaA-", "OBLIGOR", obligorReport_mercKgaa);
  }

  @Test
  void createObligorReport_PeoplesRepublicOfChina() throws IOException {
    // SCOPERATINGS-People's Republic of China-yyyy-MM-dd-OBLIGOR.xml
    verifyReportFile(451465, "SCOPERATINGS-People's Republic of China-", "OBLIGOR", obligorReport_peopleRepublicOfChina);
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

    obligorReportingService.setReportingTime(LocalDateTime.of(2020, Month.JUNE, 5, 19, 30, 40));

    File reportFile = obligorReportingService.createObligorReport(id, "./");
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