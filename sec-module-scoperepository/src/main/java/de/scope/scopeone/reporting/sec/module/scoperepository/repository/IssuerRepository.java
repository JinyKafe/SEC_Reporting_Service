package de.scope.scopeone.reporting.sec.module.scoperepository.repository;


import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Integer> {

  @Query(value = ""
      + "select i.id             reportId,\n"
      + "       'ISSUER'         reportType,\n"
      + "       i.name           issuerName,\n"
      + "       ii.identifier    scopeId,\n"
      + "       i.sec_category   secCategory,\n"
      + "       i.industry_group scopeCategory,\n"
      + "       i.created_date   createdDate\n"
      + "from issuer i\n"
      + "         left join issuer_identifier ii on i.id = ii.issuer_id", nativeQuery = true)
  List<ScopeReportMetadata> getAllIssuerRecords();

  @Query(value = ""
      + "select i.id             reportId,\n"
      + "       'ISSUER'         reportType,\n"
      + "       i.name           issuerName,\n"
      + "       ii.identifier    scopeId,\n"
      + "       i.sec_category   secCategory,\n"
      + "       i.industry_group scopeCategory,\n"
      + "       i.created_date   createdDate\n"
      + "from issuer i\n"
      + "         left join issuer_identifier ii on i.id = ii.issuer_id\n"
      + "where i.id = :reportId", nativeQuery = true)
  Optional<ScopeReportMetadata> getIssuerRecordById(@Param("reportId") Integer reportId);
}
