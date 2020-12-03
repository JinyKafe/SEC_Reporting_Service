package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.Context;
import gov.sec.ratings.PureItemType;
import gov.sec.ratings.Unit;
import java.math.BigDecimal;

public class PureItemTypeBuilder {

  private PureItemType itemType;

  public PureItemTypeBuilder() {
    itemType = ObjectFactorInitializer.getObjectFactory().createPureItemType();
  }

  public PureItemType build() {
    return itemType;
  }

  public PureItemTypeBuilder setContextRef(Context contextRef) {
    itemType.setContextRef(contextRef);
    return this;
  }

  public PureItemTypeBuilder setUnitRef(Unit unitRef) {
    itemType.setUnitRef(unitRef);
    return this;
  }

  public PureItemTypeBuilder setValue(BigDecimal value) {
    itemType.setValue(value);
    return this;
  }

  public PureItemTypeBuilder setDecimals(String value) {
    itemType.setDecimals(value);
    return this;
  }

  public PureItemTypeBuilder setPrecision(String value) {
    itemType.setPrecision(value);
    return this;
  }
}
