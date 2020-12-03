package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.SimpleType;

public final class SchemaRefBuilder {

  private SchemaRefBuilder() {
  }

  public static SimpleType newSimpleType() {
    SimpleType simpleType = ObjectFactorInitializer.getObjectFactory().createSimpleType();
    simpleType.setType("simple");
    simpleType.setHref("http://xbrl.sec.gov/rocr/2015/ratings-2015-03-31.xsd");
    return simpleType;
  }
}
