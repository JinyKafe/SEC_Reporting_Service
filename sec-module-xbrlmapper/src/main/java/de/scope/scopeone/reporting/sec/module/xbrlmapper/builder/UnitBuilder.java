package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.Unit;
import java.util.Locale;
import javax.xml.namespace.QName;

public class UnitBuilder {

  public static final String PURE = "pure";

  private Unit unit;

  public UnitBuilder() {
    unit = ObjectFactorInitializer.getObjectFactory().createUnit();
  }

  public Unit build() {
    return unit;
  }


  public UnitBuilder setId(String id) {
    String newId = "u-" + id.toLowerCase(Locale.ENGLISH);
    unit.setId(newId);
    return addMeasure(id);
  }

  private UnitBuilder addMeasure(String measure) {
    unit.getMeasure().add(new QName("http://www.xbrl.org/2003/iso4217", measure.toUpperCase(Locale.ENGLISH)));
    return this;
  }
}
