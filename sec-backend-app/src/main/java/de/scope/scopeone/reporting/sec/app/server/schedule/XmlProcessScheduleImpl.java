package de.scope.scopeone.reporting.sec.app.server.schedule;

import de.scope.scopeone.reporting.sec.app.server.controller.ReportingController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class XmlProcessScheduleImpl implements XmlProcessSchedule {

  private final ReportingController xmlProcessorFacade;

  @Scheduled(cron = "${app.config.schedule.time}")
  @SchedulerLock(name = "${app.config.schedule.name}", lockAtMostFor = "${app.config.schedule.lock.max}", lockAtLeastFor = "${app.config.schedule.lock.min}")
  @Override
  public void triggerXmlProcess() {
    xmlProcessorFacade.produceXblrFiles();
  }
}
