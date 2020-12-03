package de.scope.scopeone.reporting.sec.module.scoperepository.repository;


import de.scope.scopeone.reporting.sec.module.scoperepository.dto.ScopeReportMetadata;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObligorRepository extends JpaRepository<Obligor, Integer> {

  @Query(value = ""
      + "select o.id             reportId,\n"
      + "       'OBLIGOR'        reportType,\n"
      + "       o.name           issuerName,\n"
      + "       oi.identifier    scopeId,\n"
      + "       o.sec_category   secCategory,\n"
      + "       o.industry_group scopeCategory,\n"
      + "       o.created_date   createdDate\n"
      + "from obligor o\n"
      + "         left join obligor_identifier oi on o.id = oi.obligor_id", nativeQuery = true)
  List<ScopeReportMetadata> getAllObligorRecords();

  @Query(value = ""
      + "select o.id             reportId,\n"
      + "       'OBLIGOR'        reportType,\n"
      + "       o.name           issuerName,\n"
      + "       oi.identifier    scopeId,\n"
      + "       o.sec_category   secCategory,\n"
      + "       o.industry_group scopeCategory,\n"
      + "       o.created_date   createdDate\n"
      + "from obligor o\n"
      + "         left join obligor_identifier oi on o.id = oi.obligor_id\n"
      + "where o.id = :reportId", nativeQuery = true)
  Optional<ScopeReportMetadata> getObligorRecordById(@Param("reportId") Integer reportId);
}
