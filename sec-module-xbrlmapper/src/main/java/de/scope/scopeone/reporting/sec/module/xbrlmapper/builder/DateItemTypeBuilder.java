package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.Context;
import gov.sec.ratings.DateItemType;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

public class DateItemTypeBuilder {

  public static final String DATE_FORMAT = "yyyy-MM-dd";

  private DateItemType dateItemType;

  public DateItemTypeBuilder() {
    dateItemType = ObjectFactorInitializer.getObjectFactory().createDateItemType();
  }

  public DateItemType build() {
    return dateItemType;
  }

  public DateItemTypeBuilder setContextRef(Context contextRef) {
    dateItemType.setContextRef(contextRef);
    return this;
  }

  @SneakyThrows
  public DateItemTypeBuilder setValue(String value) {
    if (StringUtils.isEmpty(value)) {
      return this;
    }
    XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
        .newXMLGregorianCalendar(value);
    dateItemType.setValue(xmlGregorianCalendar);
    return this;

  }

  @SneakyThrows
  public DateItemTypeBuilder setValue(Date value) {
    if (value == null) {
      return this;
    }
    XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
        .newXMLGregorianCalendar(new SimpleDateFormat(DATE_FORMAT).format(value));
    dateItemType.setValue(xmlGregorianCalendar);
    return this;

  }

  @SneakyThrows
  public DateItemTypeBuilder setValue(LocalDateTime value) {
    if (value == null) {
      return this;
    }
    XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
        .newXMLGregorianCalendar(value.format(DateTimeFormatter.ISO_DATE));
    dateItemType.setValue(xmlGregorianCalendar);
    return this;

  }

}
