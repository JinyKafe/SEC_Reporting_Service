package de.scope.scopeone.reporting.sec.module.scoperepository.repository;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.InstrumentIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.InstrumentIdentifierPK;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Repository
@Validated
public interface InstrumentIdentifierRepository extends JpaRepository<InstrumentIdentifier, InstrumentIdentifierPK> {

  @Query(value = "SELECT ii FROM InstrumentIdentifier ii WHERE ii.id.instrumentId = :instrumentId")
  List<@Valid InstrumentIdentifier> findAllByInstrumentId(@Param("instrumentId") Integer instrumentId);
}
