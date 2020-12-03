package de.scope.scopeone.reporting.sec.module.xbrlgenerator.service;

import gov.sec.ratings.Xbrl;


public interface XbrlGeneratorService {

  String generateXbrlReport(Xbrl xbrl);
}
