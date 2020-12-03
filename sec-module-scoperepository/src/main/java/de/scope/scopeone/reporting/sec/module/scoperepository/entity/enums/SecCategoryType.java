package de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.TODOReminderError;
import java.util.Locale;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum SecCategoryType {
  ABCP("ABCP"),
  CDO("CDO"),
  CLO("CLO"),
  CMBS("CMBS"),
  CORPORATE("Corporate"),
  FINANCIAL("Financial"),
  INSURANCE("Insurance"),
  INT_PUBLIC("INT Public"),
  OTHER_ABS("Other ABS"),
  OTHER_SFP("Other SFP"),
  RMBS("RMBS"),
  SOVEREIGN("Sovereign"),
  US_PUBLIC("US Public");


  private final String value;

  SecCategoryType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Converter
  public static class SecCategoryTypeConverter implements AttributeConverter<SecCategoryType, String> {

    @Override
    public String convertToDatabaseColumn(SecCategoryType secCategoryType) {
      return secCategoryType.getValue();
    }

    @Override
    public SecCategoryType convertToEntityAttribute(String secCategoryValue) {
      try {
        String enumName = secCategoryValue.replace(' ', '_').toUpperCase(Locale.ENGLISH);
        return SecCategoryType.valueOf(enumName);
      } catch (IllegalArgumentException e) {
        throw new TODOReminderError(SecErrorCode.INVALID_DATA, secCategoryValue + " is not valid SecCategoryType enumeration. TODO put constraint on DB level");
      }
    }
  }
}

