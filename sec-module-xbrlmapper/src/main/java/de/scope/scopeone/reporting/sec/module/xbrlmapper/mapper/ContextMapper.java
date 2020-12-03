package de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper;

import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.ContextBuilder;
import gov.sec.ratings.Context;
import java.util.Date;
import java.util.List;

public final class ContextMapper {

  private ContextMapper() {
  }

  public static Context initializeContext(String name, List<Date> ratingsDates) {

    Date periodStartDate = null;
    Date periodEndDate = null;

    if (ratingsDates != null && !ratingsDates.isEmpty()) {
      periodStartDate = ratingsDates.get(0);
      periodEndDate = ratingsDates.get(0);
      for (int i = 1; i < ratingsDates.size(); i++) {
        if (periodStartDate.compareTo(ratingsDates.get(i)) > 0) {
          periodStartDate = ratingsDates.get(i);
        } else if (periodEndDate.compareTo(ratingsDates.get(i)) < 0) {
          periodEndDate = ratingsDates.get(i);
        }
      }
    }

    return new ContextBuilder()
        .setId("m")
        .setEntity("http://www.sec.gov/NRSRO", name)
        .setPeriodStartDate(periodStartDate)
        .setPeriodEndDate(periodEndDate)
        .build();
  }
}
