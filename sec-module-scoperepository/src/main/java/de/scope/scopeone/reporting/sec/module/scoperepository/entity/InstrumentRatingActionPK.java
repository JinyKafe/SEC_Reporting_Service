package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Embeddable
public class InstrumentRatingActionPK implements Serializable {

  private static final long serialVersionUID = 1;

  @Column(name = "INSTRUMENT_ID")
  private Integer instrumentId;

  @Column(name = "RATING_ACTION_ID")
  private String ratingActionId;

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("ratingActionId", ratingActionId)
        .toString();
  }
}
