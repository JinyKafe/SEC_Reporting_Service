package de.scope.scopeone.reporting.sec.module.xbrlgenerator.util;

import gov.sec.ratings.ObjectFactory;

public final class ObjectFactorInitializer {

  private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

  private ObjectFactorInitializer() {
  }

  public static ObjectFactory getObjectFactory() {
    return OBJECT_FACTORY;
  }
}
