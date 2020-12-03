package de.scope.scopeone.reporting.sec.module.scoperepository.repository;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.IssuerIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.IssuerIdentifierPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerIdentifierRepository extends JpaRepository<IssuerIdentifier, IssuerIdentifierPK> {

  @Query(value = "SELECT ii FROM IssuerIdentifier ii WHERE ii.id.issuerId = :issuerId")
  List<IssuerIdentifier> findAllByIssuerId(@Param("issuerId") Integer issuerId);
}
