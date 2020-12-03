package de.scope.scopeone.reporting.sec.module.scoperepository.service;

import de.scope.scopeone.reporting.sec.common.error.SecBusinessException;
import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Instrument;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.InstrumentIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.InstrumentIdentifierRepository;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.IssuerRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class IssuerJpaServiceImpl implements IssuerJpaService {

  private final IssuerRepository issuerRepository;
  private final InstrumentIdentifierRepository instrumentIdentifierRepository;

  public IssuerJpaServiceImpl(IssuerRepository issuerRepository,
                              InstrumentIdentifierRepository instrumentIdentifierRepository) {
    this.issuerRepository = issuerRepository;
    this.instrumentIdentifierRepository = instrumentIdentifierRepository;
  }

  @Override
  public @Valid Issuer findIssuerById(Integer issuerId) {

    Issuer issuer = issuerRepository.findById(issuerId)
        .orElseThrow(() -> new SecBusinessException(SecErrorCode.OBJECT_NOT_FOUND, "Unable to find Issuer#" + issuerId));
    for (Instrument instrument : issuer.getInstruments()) {
      instrument.setIdentifiers(this.findInstrumentIdentifiersByInstrumentId(instrument));
    }
    return issuer;
  }

  @Override
  public ScopeReportMetadata getIssuerRecordById(Integer issuerId) {
    return issuerRepository.getIssuerRecordById(issuerId).orElseThrow(() ->
        new SecBusinessException(SecErrorCode.OBJECT_NOT_FOUND, "Unable to find Issuer#" + issuerId));
  }

  @Override
  public List<ScopeReportMetadata> getAllIssuerRecords() {
    return issuerRepository.getAllIssuerRecords();
  }

  @Override
  public List<@Valid InstrumentIdentifier> findInstrumentIdentifiersByInstrumentId(Instrument instrument) {
    return instrumentIdentifierRepository.findAllByInstrumentId(instrument.getId());
  }
}

