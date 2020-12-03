package de.scope.scopeone.reporting.sec.module.xbrlmapper.service;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
import gov.sec.ratings.Xbrl;
import java.time.LocalDateTime;

public interface ObligorMapperService {

  Xbrl toXmlObject(Obligor obligor, LocalDateTime creationDate);
}
