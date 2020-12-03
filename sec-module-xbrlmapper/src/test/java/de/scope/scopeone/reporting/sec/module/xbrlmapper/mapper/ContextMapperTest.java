package de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper;

import gov.sec.ratings.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ContextMapperTest {

  private String name = "test-name";

  @Test
  public void startAndEndDateInDifferenceRange() {

    List<Date> dates = new ArrayList<Date>();
    String maxDate = "2020-02-11";
    String minDate = "2010-02-11";
    dates.add(parseDate(maxDate));
    dates.add(parseDate(minDate));
    dates.add(new GregorianCalendar(2019, Calendar.FEBRUARY, 11).getTime());
    Context context = ContextMapper.initializeContext(name, dates);

    Assertions.assertThat(context.getPeriod().getStartDate()).isEqualTo(minDate);
    Assertions.assertThat(context.getPeriod().getEndDate()).isEqualTo(maxDate);
  }

  @Test
  public void startAndEndDateIsSame() {

    List<Date> dates = new ArrayList<Date>();
    String date = "2020-02-11";
    dates.add(parseDate(date));
    Context context = ContextMapper.initializeContext(name, dates);

    Assertions.assertThat(context.getPeriod().getStartDate()).isEqualTo(date);
    Assertions.assertThat(context.getPeriod().getEndDate()).isEqualTo(date);
  }

  private Date parseDate(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      return null;
    }
  }

}
