package de.scope.scopeone.reporting.sec.module.scoperepository.repository;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.ObligorIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.ObligorIdentifierPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObligatorIdentifierRepository extends JpaRepository<ObligorIdentifier, ObligorIdentifierPK> {

  @Query(value = "SELECT ii FROM ObligorIdentifier ii WHERE ii.id.obligorId = :obligorId")
  List<ObligorIdentifier> findAllByObligorId(@Param("obligorId") Integer obligorId);
}
