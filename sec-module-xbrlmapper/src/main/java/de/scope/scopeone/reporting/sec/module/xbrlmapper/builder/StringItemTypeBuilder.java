package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import static de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer.getObjectFactory;

import gov.sec.ratings.Context;
import gov.sec.ratings.StringItemType;

public class StringItemTypeBuilder<I extends StringItemType> {

  private I stringItemType;

  public StringItemTypeBuilder() {

    this.stringItemType = (I) getObjectFactory().createStringItemType();
  }

  public StringItemTypeBuilder(I stringItemType) {
    this.stringItemType = stringItemType;
  }

  public I build() {
    return stringItemType;
  }


  public StringItemTypeBuilder setContextRef(Context context) {
    stringItemType.setContextRef(context);
    return this;
  }

  public StringItemTypeBuilder setValue(String value) {
    stringItemType.setValue(value);
    return this;
  }

}
