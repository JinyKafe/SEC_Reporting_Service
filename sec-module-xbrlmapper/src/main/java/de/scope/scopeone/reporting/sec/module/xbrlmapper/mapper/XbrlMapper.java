package de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper;

import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.RocraBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.SchemaRefBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.UnitBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.XbrlBuilder;
import gov.sec.ratings.Context;
import gov.sec.ratings.ISD;
import gov.sec.ratings.OD;
import gov.sec.ratings.ROCRA;
import gov.sec.ratings.Unit;
import gov.sec.ratings.Xbrl;
import java.time.LocalDateTime;
import java.util.Map;

public final class XbrlMapper {

  private XbrlMapper() {
  }

  public static Xbrl toXmlObject(Object ob, Context contextRef, String agencyName, LocalDateTime creatingDate) {
    ROCRA rocra = getRocra(agencyName, ob, contextRef, creatingDate);

    return new XbrlBuilder()
        .addSchemaRef(SchemaRefBuilder.newSimpleType())
        .addContext(contextRef)
        .addRocra(rocra)
        .build();
  }

  public static Xbrl toXmlObject(Object ob, Context contextRef, String agencyName, Map<String, Unit> unitMap, LocalDateTime creationDate) {
    ROCRA rocra = getRocra(agencyName, ob, contextRef, creationDate);

    XbrlBuilder builder = new XbrlBuilder()
        .addSchemaRef(SchemaRefBuilder.newSimpleType())
        .addContext(contextRef);

    if (unitMap.containsKey(UnitBuilder.PURE)) {
      builder.addUnit(unitMap.remove(UnitBuilder.PURE));
    }

    unitMap.keySet().forEach(key -> builder.addUnit(unitMap.get(key)));

    return builder.addRocra(rocra).build();
  }

  private static ROCRA getRocra(String agencyName, Object ob, Context contextRef, LocalDateTime creationDate) {
    RocraBuilder builder = new RocraBuilder()
        .addContextRef(contextRef)
        .addRAN(agencyName)
        .setFCD(creationDate);
    if (ob instanceof OD) {
      builder.addOD((OD) ob);
    } else if (ob instanceof ISD) {
      builder.addISD((ISD) ob);
    }
    return builder.build();
  }

}
