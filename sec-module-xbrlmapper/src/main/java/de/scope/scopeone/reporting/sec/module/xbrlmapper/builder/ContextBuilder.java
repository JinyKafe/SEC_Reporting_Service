package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.Context;
import gov.sec.ratings.ContextEntityType;
import gov.sec.ratings.ContextEntityType.Identifier;
import gov.sec.ratings.ContextPeriodType;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class ContextBuilder {

  public static final String DATE_FORMAT = "yyyy-MM-dd";

  private Context context;

  public ContextBuilder() {
    context = ObjectFactorInitializer.getObjectFactory().createContext();
  }

  public Context build() {
    return context;
  }


  public ContextBuilder setId(String id) {
    context.setId(id);
    return this;
  }

  public ContextBuilder setEntity(String schema, String value) {
    ContextEntityType contextEntityType = createContextEntityType(schema, value);
    context.setEntity(contextEntityType);
    return this;
  }

  public ContextBuilder setPeriodStartDate(String date) {
    if (StringUtils.isEmpty(date)) {
      return this;
    }
    getContextPeriodType().setStartDate(date);
    return this;
  }

  public ContextBuilder setPeriodStartDate(Date date) {
    if (Objects.isNull(date)) {
      return this;
    }
    getContextPeriodType().setStartDate(new SimpleDateFormat(DATE_FORMAT).format(date));
    return this;
  }

  public ContextBuilder setPeriodStartDate(LocalDateTime date) {
    if (Objects.isNull(date)) {
      return this;
    }
    getContextPeriodType().setStartDate(new SimpleDateFormat(DATE_FORMAT).format(date));
    return this;
  }

  public ContextBuilder setPeriodEndDate(String date) {
    if (StringUtils.isEmpty(date)) {
      return this;
    }
    getContextPeriodType().setEndDate(date);
    return this;
  }

  public ContextBuilder setPeriodEndDate(Date date) {
    if (StringUtils.isEmpty(date)) {
      return this;
    }
    return setPeriodEndDate(new SimpleDateFormat(DATE_FORMAT).format(date));
  }

  private static ContextEntityType createContextEntityType(String schema, String value) {
    ContextEntityType contextEntityType = ObjectFactorInitializer.getObjectFactory().createContextEntityType();
    Identifier identifier = createIdentifier(schema, value);
    contextEntityType.setIdentifier(identifier);
    return contextEntityType;
  }

  private static Identifier createIdentifier(String schema, String value) {
    Identifier identifier = ObjectFactorInitializer.getObjectFactory().createContextEntityTypeIdentifier();
    identifier.setScheme(schema);
    identifier.setValue(value);
    return identifier;
  }

  private ContextPeriodType getContextPeriodType() {
    if (Objects.isNull(context.getPeriod())) {
      context.setPeriod(ObjectFactorInitializer.getObjectFactory().createContextPeriodType());
    }
    return context.getPeriod();
  }
}
