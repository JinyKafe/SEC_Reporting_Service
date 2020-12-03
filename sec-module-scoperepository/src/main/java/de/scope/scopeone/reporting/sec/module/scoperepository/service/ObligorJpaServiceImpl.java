package de.scope.scopeone.reporting.sec.module.scoperepository.service;

import de.scope.scopeone.reporting.sec.common.error.SecBusinessException;
import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.ObligorRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ObligorJpaServiceImpl implements ObligorJpaService {

  private final ObligorRepository obligorRepository;

  public ObligorJpaServiceImpl(ObligorRepository obligorRepository) {
    this.obligorRepository = obligorRepository;
  }

  @Override
  public @Valid Obligor findObligorById(Integer obligorId) {
    return obligorRepository.findById(obligorId).orElseThrow(() ->
        new SecBusinessException(SecErrorCode.OBJECT_NOT_FOUND, "Unable to find Obligor#" + obligorId));
  }

  @Override
  public ScopeReportMetadata getObligorRecordById(Integer obligorId) {
    return obligorRepository.getObligorRecordById(obligorId).orElseThrow(() ->
        new SecBusinessException(SecErrorCode.OBJECT_NOT_FOUND, "Unable to find Obligor#" + obligorId));
  }

  @Override
  public List<ScopeReportMetadata> getAllObligorRecords() {
    return obligorRepository.getAllObligorRecords();
  }
}

