package de.scope.scopeone.reporting.sec.module.scoperepository.service;


import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Instrument;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.InstrumentIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import java.util.List;

public interface IssuerJpaService {

  Issuer findIssuerById(Integer issuerId);

  ScopeReportMetadata getIssuerRecordById(Integer issuerId);

  List<ScopeReportMetadata> getAllIssuerRecords();

  List<InstrumentIdentifier> findInstrumentIdentifiersByInstrumentId(Instrument instrument);
}
