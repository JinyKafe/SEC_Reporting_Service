package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import gov.sec.ratings.DateItemType;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateItemTypeBuilderTest {

  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


  @Test
  public void testDateFormatInDifferentFormatAsExpected() {
    DateItemType dateItemType = new DateItemTypeBuilder().setValue(toDate("29/02/2020")).build();
    Assertions.assertThat(dateItemType.getValue().toString()).isEqualTo("2020-02-29");
  }

  @Test
  @SneakyThrows
  public void testLocalDateTimeFormatInDifferentFormatAsExpected() {
    LocalDateTime localDateTime = LocalDateTime.of(2020, Month.FEBRUARY, 29, 1, 1);
    DateItemType dateItemType = new DateItemTypeBuilder().setValue(localDateTime).build();
    Assertions.assertThat(dateItemType.getValue().toString()).isEqualTo("2020-02-29");
  }


  @SneakyThrows
  private Date toDate(String date) {
    return formatter.parse(date);
  }

}
