package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.SecCategoryType;

public interface ScopeReportJpaEntity {

  Integer getId();

  SecCategoryType getSecCategory();

  String getName();

  String getIndustryGroup();

  String getLei();

  String getCik();

  CreditRatingAgencyInfo getRatingAgencyInfo();
}
