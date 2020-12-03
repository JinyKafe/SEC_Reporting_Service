package de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.TODOReminderError;
import java.util.Locale;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum RatedObjectType {

  INSTRUMENT("Instrument"),
  OTHER("Other"),
  PROGRAM("Program"),
  SHELF("Shelf");

  private final String value;

  RatedObjectType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Converter
  public static class RatedObjectTypeConverter implements AttributeConverter<RatedObjectType, String> {

    @Override
    public String convertToDatabaseColumn(RatedObjectType secCategoryType) {
      return secCategoryType.getValue();
    }

    @Override
    public RatedObjectType convertToEntityAttribute(String secCategoryValue) {
      try {
        return RatedObjectType.valueOf(secCategoryValue.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException e) {
        throw new TODOReminderError(SecErrorCode.INVALID_DATA, secCategoryValue + " is not valid SecCategoryType enumeration. TODO put constraint on DB level");
      }
    }
  }
}

