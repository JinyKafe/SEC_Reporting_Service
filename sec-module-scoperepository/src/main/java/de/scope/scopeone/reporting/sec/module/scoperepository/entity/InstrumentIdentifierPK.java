package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.InstrumentIdentifierSchemeType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Embeddable
public class InstrumentIdentifierPK implements Serializable {

  private static final long serialVersionUID = 1;

  @Column(name = "INSTRUMENT_ID")
  private Integer instrumentId;

  @Enumerated(EnumType.STRING)
  @Column(name = "IDENTIFIER_SCHEME")
  private InstrumentIdentifierSchemeType identifierScheme;

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("identifierScheme", identifierScheme)
        .toString();
  }

}
