package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.BooleanItemType;
import gov.sec.ratings.Context;

public class BooleanItemTypeBuilder {

  private BooleanItemType booleanItemType;

  public BooleanItemTypeBuilder() {
    booleanItemType = ObjectFactorInitializer.getObjectFactory().createBooleanItemType();
  }

  public BooleanItemType build() {
    return booleanItemType;
  }

  public BooleanItemTypeBuilder setContextRef(Context contextRef) {
    booleanItemType.setContextRef(contextRef);
    return this;
  }

  public BooleanItemTypeBuilder setValue(boolean value) {
    booleanItemType.setValue(value);
    return this;

  }

}
