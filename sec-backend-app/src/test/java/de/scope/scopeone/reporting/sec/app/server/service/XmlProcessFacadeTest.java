package de.scope.scopeone.reporting.sec.app.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.scope.scopeone.reporting.sec.app.server.conf.ZipConfigProperties;
import de.scope.scopeone.reporting.sec.app.server.controller.ReportingControllerImpl;
import de.scope.scopeone.reporting.sec.service.ReportingService;
import de.scope.scopeone.reporting.sec.service.ZippingService;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class XmlProcessFacadeTest {

  @Mock
  private ReportingService reportingService;
  @Mock
  private ZippingService zippingService;
  @Mock
  private ZipConfigProperties zipConfigProperties;
  @InjectMocks
  private ReportingControllerImpl xmlProcessFacade;

  private final String filePrefix = "new application zip";

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(zipConfigProperties);
  }

  @Test
  void createNewName() {
    when(zipConfigProperties.getNameprefix()).thenReturn(filePrefix);

    String fileName = xmlProcessFacade.getNewFileName();
    assertThat(fileName).startsWith(String.format("%s-%s", filePrefix, LocalDate.now().toString()));

    verify(zipConfigProperties).getNameprefix();
  }

}
