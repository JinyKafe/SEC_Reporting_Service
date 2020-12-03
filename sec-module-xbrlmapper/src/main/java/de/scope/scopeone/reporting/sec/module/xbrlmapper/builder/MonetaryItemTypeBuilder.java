package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.Context;
import gov.sec.ratings.MonetaryItemType;
import gov.sec.ratings.Unit;
import java.math.BigDecimal;

public class MonetaryItemTypeBuilder {

  private MonetaryItemType itemType;

  public MonetaryItemTypeBuilder() {
    itemType = ObjectFactorInitializer.getObjectFactory().createMonetaryItemType();
  }

  public MonetaryItemType build() {
    return itemType;
  }

  public MonetaryItemTypeBuilder setContextRef(Context contextRef) {
    itemType.setContextRef(contextRef);
    return this;
  }

  public MonetaryItemTypeBuilder setUnitRef(Unit unitRef) {
    itemType.setUnitRef(unitRef);
    return this;
  }

  public MonetaryItemTypeBuilder setValue(BigDecimal value) {
    itemType.setValue(value);
    return this;
  }

  public MonetaryItemTypeBuilder setDecimals(String value) {
    itemType.setDecimals(value);
    return this;
  }

  public MonetaryItemTypeBuilder setPrecision(String value) {
    itemType.setPrecision(value);
    return this;
  }
}
