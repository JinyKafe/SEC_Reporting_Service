package de.scope.scopeone.reporting.sec.module.scoperepository.service;


import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
import java.util.List;

public interface ObligorJpaService {

  Obligor findObligorById(Integer obligorId);

  ScopeReportMetadata getObligorRecordById(Integer obligorId);

  List<ScopeReportMetadata> getAllObligorRecords();
}
