package de.scope.scopeone.reporting.sec.module.xbrlmapper.service;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import gov.sec.ratings.Xbrl;
import java.time.LocalDateTime;

public interface IssuerMapperSerivce {

  Xbrl toXmlObject(Issuer issuer, LocalDateTime creationDate);
}
